package com.education.cocktails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.education.cocktails.network.NetworkBound
import com.education.cocktails.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NetworkBoundTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var handleSaveCallResult: (A?) -> Unit
    private lateinit var handleShouldFetch:(A?) -> Boolean
    private lateinit var handleCreateCall:() -> LiveData<Response<A>>
    private val dbData = MutableLiveData<A>()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val fetchedOnce = AtomicBoolean(false)

    private lateinit var networkBound: NetworkBound<A,A>

    private data class A(var value: Int)

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        networkBound = object : NetworkBound<A,A>(testScope) {
            override fun saveCallResult(item: A?) {
                handleSaveCallResult(item)
            }

            override fun shouldFetch(data: A?): Boolean {
                return handleShouldFetch(data) && fetchedOnce.compareAndSet(false, true)
            }

            override fun loadFromDb(): LiveData<A> {
                return dbData
            }

            override fun createCall(): LiveData<Response<A>> {
                return handleCreateCall()
            }

        }
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun basicNetworkFetching_Test() {
        val saved = AtomicReference<A>()
        handleShouldFetch = { it == null }
        val fetchedDbA = A(1)
        handleSaveCallResult = { a ->
            saved.set(a)
            dbData.value = fetchedDbA
        }

        val networkResult = A(1)
        handleCreateCall = { MutableLiveData<Response<A>>().apply { value = Response.success(networkResult) }}

        val observer = mock<Observer<Resource<A>>>()
        networkBound.asLiveData().observeForever(observer)
        waitForCoroutineWork()

        Mockito.verify(observer).onChanged(Resource.loading(null))

        dbData.value = null

        waitForCoroutineWork()

        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(networkResult))
        Mockito.verify(observer).onChanged(Resource.success(fetchedDbA))
    }

    private fun waitForCoroutineWork() = runBlocking {
        //testScope.currentJob.join()
        delay(1000)
    }

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
}
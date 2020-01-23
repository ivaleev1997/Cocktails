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
import okhttp3.MediaType
import okhttp3.ResponseBody
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
        waitFor()

        Mockito.verify(observer).onChanged(Resource.loading(null))

        dbData.value = null

        waitFor()

        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(networkResult))
        Mockito.verify(observer).onChanged(Resource.success(fetchedDbA))
    }

    @Test
    fun failureNetworkFetching_Test() {
        val saved = AtomicBoolean(false)
        handleShouldFetch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }

        val body = ResponseBody.create(MediaType.parse("text/html"), "error")
        val errorResponse = Response.error<A>(500, body)
        handleCreateCall = {MutableLiveData<Response<A>>()
            .apply { value =  errorResponse}}

        val observer = mock<Observer<Resource<A>>>()
        networkBound.asLiveData().observeForever(observer)
        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(null))

        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
        dbData.value = null
        waitFor()
        Mockito.verify(observer).onChanged(Resource.error("error", null))
    }

    @Test
    fun dbSuccessWithoutNetwork_Test() {
        val saved = AtomicBoolean(false)
        handleShouldFetch = {it == null}
        handleSaveCallResult = {
            saved.set(true)
        }

        val storedDbData1 = A(1)
        val storedDbData2 = A(2)
        val observer = mock<Observer<Resource<A>>>()
        networkBound.asLiveData().observeForever(observer)

        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(null))

        dbData.value = storedDbData1

        waitFor()
        Mockito.verify(observer).onChanged(Resource.success(storedDbData1))
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))

        dbData.value = storedDbData2
        Mockito.verify(observer).onChanged(Resource.success(storedDbData2))
    }

    @Test
    fun dbSuccessNetworkError_Test() {
        val saved = AtomicBoolean(false)
        val dbValue = A(3)
        handleShouldFetch = { a -> a === dbValue }
        handleSaveCallResult = {
            saved.set(true)
        }

        val apiResponseLiveData = MutableLiveData<Response<A>>()
        handleCreateCall = { apiResponseLiveData }
        val body = ResponseBody.create(MediaType.parse("text/html"), "error")
        val observer = mock<Observer<Resource<A>>>()

        networkBound.asLiveData().observeForever(observer)
        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(null))

        dbData.value = dbValue
        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(dbValue))

        apiResponseLiveData.value = Response.error(500, body)
        waitFor()
        Mockito.verify(observer).onChanged(Resource.error("error", dbValue))

        val nextDbValue = A(5)
        dbData.value = nextDbValue
        waitFor()
        Mockito.verify(observer).onChanged(Resource.error("error", nextDbValue))
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))

        Mockito.verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithSuccessReFetch_Test() {
        val dbValue = A(101)
        val dbValue2 = A(104)
        val saved = AtomicReference<A>()

        handleShouldFetch = { a -> a === dbValue }
        handleSaveCallResult = { a ->
            saved.set(a)
            dbData.value = dbValue2
        }

        val apiResponseLiveData = MutableLiveData<Response<A>>()
        handleCreateCall = { apiResponseLiveData }
        val observer = mock<Observer<Resource<A>>>()
        networkBound.asLiveData().observeForever(observer)
        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(null))

        dbData.value = dbValue
        waitFor()
        Mockito.verify(observer).onChanged(Resource.loading(dbValue))

        val networkResult = A(101)
        apiResponseLiveData.value = Response.success(networkResult)
        waitFor()
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(networkResult))
        Mockito.verify(observer).onChanged(Resource.success(dbValue2))
    }

    private fun waitFor() = runBlocking {
        //testScope.currentJob.join()
        delay(1000)
    }

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
}
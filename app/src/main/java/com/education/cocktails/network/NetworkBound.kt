package com.education.cocktails.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class NetworkBound<RequestType, ResultType>
    @MainThread constructor(private val uiScope: CoroutineScope) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        println(Thread.currentThread())
        result.value = Resource.loading(null)

        val dbSource = loadFromDb()

        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { dbData ->
                    setValue(Resource.success(dbData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()

        result.addSource(dbSource) { data ->
            setValue(Resource.loading(data))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(dbSource)
            result.removeSource(apiResponse)

            if (response.isSuccessful) {
                uiScope.launch {
                    //save response result
                    withContext(Dispatchers.IO) {
                        saveCallResult(processResponse(response))
                    }
                    //reload new data from Db
                    result.addSource(loadFromDb()) { newData ->
                        println(newData)
                        setValue(Resource.success(newData))
                    }
                }
            } else {
                onFetchFailed()
                val errorMsg = errorResponseMessage(response)
                uiScope.launch {
                    result.addSource(dbSource) { data ->
                        setValue(Resource.error(errorMsg, data))
                    }
                }
            }
        }
    }

    private fun errorResponseMessage(response: Response<RequestType>) =
            response.errorBody()?.string() ?: response.message() ?: ""


    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: Response<RequestType>) = response.body()

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType?)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<Response<RequestType>>
}
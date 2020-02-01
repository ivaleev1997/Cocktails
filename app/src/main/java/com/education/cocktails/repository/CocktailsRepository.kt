package com.education.cocktails.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.education.cocktails.db.CocktailsDb
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.TheRemoteDBResponse
import com.education.cocktails.network.NetworkBound
import com.education.cocktails.network.Resource
import com.education.cocktails.network.TheCocktailsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class CocktailsRepository
@Inject constructor(cocktailsDb: CocktailsDb, val cocktailsApi: TheCocktailsApi) {

    lateinit var currentCoroutineScope: CoroutineScope
    private val cocktailDao = cocktailsDb.getCocktailDao()

    fun loadCocktails(): LiveData<Resource<List<Cocktail>>> =
        object : NetworkBound<TheRemoteDBResponse, List<Cocktail>>(currentCoroutineScope) {
            override fun saveCallResult(item: TheRemoteDBResponse?) {
                val drinks = item?.drinks
                if (!drinks.isNullOrEmpty())
                    cocktailDao.insertCocktails(drinks)
            }
            //TODO Crate DataFreshnessChecker
            override fun shouldFetch(data: List<Cocktail>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<Cocktail>> {
                return cocktailDao.getCocktails()
            }

            override fun createCall(): LiveData<Response<TheRemoteDBResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBResponse>>()
                currentCoroutineScope.launch {
                    val response = async(Dispatchers.IO) {
                        cocktailsApi.filterByCategoryAsync("Cocktail")
                    }

                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }
        }.asLiveData()

    fun loadCocktailById(id: Long): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBound<TheRemoteDBResponse, List<Cocktail>>(currentCoroutineScope) {
            override fun saveCallResult(item: TheRemoteDBResponse?) {
                val drinks = item?.drinks
                if (!drinks.isNullOrEmpty()) {
                    cocktailDao.insertCocktails(drinks)
                }
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data?.any {
                    it.instructions == null
                } ?: true
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> {
                return cocktailDao.getCocktailById(id)
            }

            override fun createCall(): LiveData<Response<TheRemoteDBResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBResponse>>()
                currentCoroutineScope.launch {
                    val response = async(Dispatchers.IO) {
                        cocktailsApi.getFullDetailsByIdAsync(id)
                    }
                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }
        }.asLiveData()
    }
}
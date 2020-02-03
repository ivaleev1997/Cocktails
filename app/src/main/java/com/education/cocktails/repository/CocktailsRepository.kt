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
                if (!drinks.isNullOrEmpty()) {
                    val favorites = cocktailDao.getFavoriteCocktails()
                    cocktailDao.insertCocktails(updateNewCocktailsList(favorites, drinks))
                }
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

    private fun updateNewCocktailsList(favorites: List<Cocktail>, newData: List<Cocktail>): List<Cocktail> {
        val drinks = newData.toMutableList()
        favorites.forEach {favorite ->
            drinks.map {newData ->
                if (newData.idDrink == favorite.idDrink)
                    newData.favorite = favorite.favorite
            }
        }

        return drinks
    }

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
                val mutableLiveData = MutableLiveData<List<Cocktail>>()
                currentCoroutineScope.launch {
                    val cocktail = async(Dispatchers.IO) {
                        cocktailDao.getCocktailById(id)
                    }
                    mutableLiveData.value = cocktail.await()
                }

                return mutableLiveData
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

    fun changeFavoriteFlag(id: Long, flag: Boolean) {
        currentCoroutineScope.launch(Dispatchers.IO) {
            val cocktailList = cocktailDao.getCocktailById(id)
            if (cocktailList.isNotEmpty()) {
                cocktailDao.insertCocktail(cocktailList.first().apply { favorite = flag })
            }
        }
    }
}
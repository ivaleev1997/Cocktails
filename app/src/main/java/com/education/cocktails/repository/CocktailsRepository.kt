package com.education.cocktails.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.cocktails.db.CocktailsDb
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.CocktailsIngredient
import com.education.cocktails.model.TheRemoteDBCocktailsResponse
import com.education.cocktails.model.TheRemoteDBIngredientsResponse
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

    private val cocktailDao = cocktailsDb.getCocktailDao()
    private val ingredientDao = cocktailsDb.getIngredientDao()

    fun loadCocktails(coroutineScope: CoroutineScope): LiveData<Resource<List<Cocktail>>> =
        object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(coroutineScope) {
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
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

            override fun createCall(): LiveData<Response<TheRemoteDBCocktailsResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBCocktailsResponse>>()
                coroutineScope.launch {
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

    fun loadCocktailById(id: Long, coroutineScope: CoroutineScope): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(coroutineScope) {
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
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
                coroutineScope.launch {
                    val cocktail = async(Dispatchers.IO) {
                        cocktailDao.getCocktailById(id)
                    }
                    mutableLiveData.value = cocktail.await()
                }

                return mutableLiveData
            }

            override fun createCall(): LiveData<Response<TheRemoteDBCocktailsResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBCocktailsResponse>>()
                coroutineScope.launch {
                    val response = async(Dispatchers.IO) {
                        cocktailsApi.getFullDetailsByIdAsync(id)
                    }
                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }
        }.asLiveData()
    }

    fun changeFavoriteFlag(id: Long, flag: Boolean, coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            val cocktailList = cocktailDao.getCocktailById(id)
            if (cocktailList.isNotEmpty()) {
                cocktailDao.insertCocktail(cocktailList.first().apply { favorite = flag })
            }
        }
    }

    fun loadFavoriteCocktails(coroutineScope: CoroutineScope): LiveData<List<Cocktail>> {
        val mutableLiveData = MutableLiveData<List<Cocktail>>()
        coroutineScope.launch {
            val listFavorites = async(Dispatchers.IO) {
                cocktailDao.getFavoriteCocktails()
            }

            mutableLiveData.value = listFavorites.await()
        }

        return mutableLiveData
    }

    fun loadIngredients(uiScope: CoroutineScope): LiveData<Resource<List<CocktailsIngredient>>> {
        return object : NetworkBound<TheRemoteDBIngredientsResponse, List<CocktailsIngredient>>(uiScope) {
            override fun saveCallResult(item: TheRemoteDBIngredientsResponse?) {
                val ingredients = item?.drinks
                if (!ingredients.isNullOrEmpty()) {
                    ingredientDao.insertAll(ingredients)
                }
            }

            override fun shouldFetch(data: List<CocktailsIngredient>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<CocktailsIngredient>> {
                return ingredientDao.getIngredients()
            }

            override fun createCall(): LiveData<Response<TheRemoteDBIngredientsResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBIngredientsResponse>>()
                uiScope.launch {
                    val response = async(Dispatchers.IO) {
                        cocktailsApi.getListIngredients()
                    }
                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }

        }.asLiveData()
    }

    // Works only with Internet because implementation hasn't searching in local Db by ingredients
    fun loadCocktailsByIngredientName(uiScope: CoroutineScope, ingredient: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(uiScope) {
            private var fetchedList = listOf<Cocktail>()
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
                val cocktails = item?.drinks
                if (!cocktails.isNullOrEmpty()) {
                    cocktailDao.insertCocktails(cocktails)
                    fetchedList = cocktails
                }
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean = true

            override fun loadFromDb(): LiveData<List<Cocktail>> =
                if (fetchedList.isNullOrEmpty())
                    cocktailDao.getCocktails()
                else {
                    val mediatorLiveData = MediatorLiveData<List<Cocktail>>()
                    val dBSource = cocktailDao.getCocktails()
                    mediatorLiveData.addSource(dBSource) { cocktails ->
                        mediatorLiveData.value = cocktails.filter {
                            fetchedList.contains(it)
                        }
                    }
                    mediatorLiveData
                }

            override fun createCall(): LiveData<Response<TheRemoteDBCocktailsResponse>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBCocktailsResponse>>()
                uiScope.launch {
                    val response = async(Dispatchers.IO) {
                        cocktailsApi.filterByIngredientAsync(ingredient)
                    }
                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }
        }.asLiveData()
    }
}
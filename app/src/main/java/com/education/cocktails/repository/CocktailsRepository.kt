package com.education.cocktails.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.cocktails.db.CocktailsDb
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.TheRemoteDBCocktailsResponse
import com.education.cocktails.network.Resource
import com.education.cocktails.network.TheCocktailsApi
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocktailsRepository
@Inject constructor(cocktailsDb: CocktailsDb, val cocktailsApi: TheCocktailsApi) {

    private val cocktailDao = cocktailsDb.getCocktailDao()

    fun loadCocktails(coroutineScope: CoroutineScope): LiveData<Resource<List<Cocktail>>> =
        object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(coroutineScope) {
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
                saveCallResultCocktails(item?.drinks)
            }
            //TODO Crate DataFreshnessChecker
            override fun shouldFetch(data: List<Cocktail>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<Cocktail>> {
                return cocktailDao.getCocktails()
            }

            override fun createCall(): LiveData<Response<TheRemoteDBCocktailsResponse>> {
                return createCallForMainCocktailsList(coroutineScope)
            }
        }.asLiveData()

    fun loadCocktailById(id: Long, coroutineScope: CoroutineScope): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(coroutineScope) {
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
                saveCallResultCocktails(item?.drinks)
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

    fun loadCocktailsByIngredientNamesList(coroutineScope: CoroutineScope, ingredientNames: List<String>): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBound<TheRemoteDBCocktailsResponse, List<Cocktail>>(coroutineScope) {
            private var fetchedList = listOf<Cocktail>()
            override fun saveCallResult(item: TheRemoteDBCocktailsResponse?) {
                fetchedList = item?.drinks ?: listOf()
                saveCallResultCocktails(fetchedList)
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
                coroutineScope.launch {
                    val responsesList = ingredientNames.map {
                        async(Dispatchers.IO) {
                            cocktailsApi.filterByIngredientAsync(it)
                        }
                    }
                    mutableLiveData.value = combineRemoteResponseList(responsesList.awaitAll())
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
                fetchedList = item?.drinks ?: listOf()
                saveCallResultCocktails(fetchedList)
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

    fun loadCocktailsByIds(listId: List<Long>): LiveData<List<Cocktail>> {
        return cocktailDao.getCocktailsByIds(listId)
    }

    private fun createCallForMainCocktailsList(coroutineScope: CoroutineScope): LiveData<Response<TheRemoteDBCocktailsResponse>> {
        val mutableLiveData = MutableLiveData<Response<TheRemoteDBCocktailsResponse>>()
        coroutineScope.launch {
            val response1 = async(Dispatchers.IO) {
                cocktailsApi.filterByCategoryAsync("Cocktail")
            }
            val response2 = async(Dispatchers.IO) {
                cocktailsApi.filterByCategoryAsync("Ordinary_Drink")
            }

            mutableLiveData.value = combineRemoteResponse(response1.await(), response2.await())
        }

        return mutableLiveData
    }

    private fun combineRemoteResponse(
        responseFirst: Response<TheRemoteDBCocktailsResponse>,
        responseSecond: Response<TheRemoteDBCocktailsResponse>
    ): Response<TheRemoteDBCocktailsResponse>  =
        if (responseFirst.isSuccessful || responseSecond.isSuccessful) {
            Response.success(combineListsFromResponse(responseFirst, responseSecond))
        } else responseFirst

    private fun combineListsFromResponse(
        responseFirst: Response<TheRemoteDBCocktailsResponse>,
        responseSecond: Response<TheRemoteDBCocktailsResponse>
    ): TheRemoteDBCocktailsResponse {
        val firstList = responseFirst.body()?.drinks ?: listOf()
        val secondList = responseSecond.body()?.drinks ?: listOf()
        val resultSet = (firstList + secondList).toSet()

        return TheRemoteDBCocktailsResponse(resultSet.toList())
    }

    private fun combineRemoteResponseList(
        responses: List<Response<TheRemoteDBCocktailsResponse>>
    ): Response<TheRemoteDBCocktailsResponse> =
        if (responses.any { it.isSuccessful }) {
            Response.success(combineListsFromResponse(responses))
        } else
            responses.first()

    private fun combineListsFromResponse(
        responses: List<Response<TheRemoteDBCocktailsResponse>>
    ): TheRemoteDBCocktailsResponse {
        val result = mutableListOf<Cocktail>()
        responses.forEach {
            if (it.body()?.drinks != null)
                result += it.body()!!.drinks
        }

        val resultSet = result.toSet()

        return TheRemoteDBCocktailsResponse(resultSet.toList())
    }

    private fun saveCallResultCocktails(drinks: List<Cocktail>?) {
        if (!drinks.isNullOrEmpty()) {
            val favorites = cocktailDao.getFavoriteCocktails()
            cocktailDao.insertCocktails(updateNewCocktailsList(favorites, drinks))
        }
    }

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
}
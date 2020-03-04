package com.education.details.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.education.core_api.database.CocktailDao
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Resource
import com.education.core_api.dto.TheRemoteDBResponse
import com.education.core_api.network.TheCocktailsDbApi
import com.education.core_api.repository.NetworkResourceBound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class DetailsRepositoryImpl
@Inject constructor(
    private val cocktailDao: CocktailDao,
    private val theCocktailsDbApi: TheCocktailsDbApi
) : DetailsRepository {

    override fun loadCocktailById(
        id: Long,
        coroutineScope: CoroutineScope
    ): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkResourceBound<TheRemoteDBResponse<Cocktail>, List<Cocktail>>(coroutineScope) {
            override fun saveCallResult(item: TheRemoteDBResponse<Cocktail>?) {
                saveCallResultCocktails(item?.drinks)
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data?.any {
                    it.instructions == null
                } ?: true
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> {
                return cocktailDao.getCocktailById(id)
            }

            override fun createCall(): LiveData<Response<TheRemoteDBResponse<Cocktail>>> {
                val mutableLiveData = MutableLiveData<Response<TheRemoteDBResponse<Cocktail>>>()
                coroutineScope.launch {
                    val response = async(Dispatchers.IO) {
                        theCocktailsDbApi.getFullDetailsByIdAsync(id)
                    }
                    mutableLiveData.value = response.await()
                }

                return mutableLiveData
            }
        }.asLiveData()
    }

    override fun saveCocktail(cocktail: Cocktail, coroutineScope: CoroutineScope) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
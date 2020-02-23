package com.education.cocktails.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.education.cocktails.db.CocktailsDb
import com.education.cocktails.model.Ingredient
import com.education.cocktails.model.TheRemoteDBIngredientsResponse
import com.education.cocktails.network.Resource
import com.education.cocktails.network.TheCocktailsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRepository
@Inject constructor(
    cocktailsDb: CocktailsDb,
    val cocktailsApi: TheCocktailsApi
) {
    private val ingredientDao = cocktailsDb.getIngredientDao()

    fun loadIngredients(uiScope: CoroutineScope): LiveData<Resource<List<Ingredient>>> {
        return object : NetworkBound<TheRemoteDBIngredientsResponse, List<Ingredient>>(uiScope) {
            override fun saveCallResult(item: TheRemoteDBIngredientsResponse?) {
                val ingredients = item?.drinks
                if (!ingredients.isNullOrEmpty()) {
                    ingredientDao.insertAll(ingredients)
                }
            }

            override fun shouldFetch(data: List<Ingredient>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<Ingredient>> {
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
}
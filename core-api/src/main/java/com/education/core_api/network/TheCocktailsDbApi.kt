package com.education.core_api.network

import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Ingredient
import com.education.core_api.dto.TheRemoteDBResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailsDbApi {
    @GET("random.php")
    suspend fun getRandomCocktailAsync()
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("search.php")
    suspend fun searchByNameAsync(@Query("s") name: String)
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("search.php")
    suspend fun searchByIngredientAsync(@Query("i") name: String)
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("lookup.php")
    suspend fun getFullDetailsByIdAsync(@Query("i") id: Long)
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("filter.php")
    suspend fun filterByCategoryAsync(@Query("c") category: String)
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("filter.php")
    suspend fun filterByIngredientAsync(@Query("i") ingredient: String)
            : Response<TheRemoteDBResponse<Cocktail>>

    @GET("list.php?i=list")
    suspend fun getListIngredients()
            : Response<TheRemoteDBResponse<Ingredient>>
}
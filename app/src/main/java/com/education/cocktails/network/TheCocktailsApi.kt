package com.education.cocktails.network

import com.education.cocktails.model.TheRemoteDBCocktailsResponse
import com.education.cocktails.model.TheRemoteDBIngredientsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailsApi {
    @GET("random.php")
    suspend fun getRandomAsync()
            : Response<TheRemoteDBCocktailsResponse>

    @GET("search.php")
    suspend fun searchByNameAsync(@Query("s") name: String)
            : Response<TheRemoteDBCocktailsResponse>

    @GET("search.php")
    suspend fun searchByIngredientAsync(@Query("i") name: String)
            : Response<TheRemoteDBCocktailsResponse>

    @GET("lookup.php")
    suspend fun getFullDetailsByIdAsync(@Query("i") id: Long)
            : Response<TheRemoteDBCocktailsResponse>

    @GET("filter.php")
    suspend fun filterByCategoryAsync(@Query("c") category: String)
            : Response<TheRemoteDBCocktailsResponse>

    @GET("filter.php")
    suspend fun filterByIngredientAsync(@Query("i") ingredient: String)
            : Response<TheRemoteDBCocktailsResponse>

    @GET("list.php?i=list")
    suspend fun getListIngredients()
            : Response<TheRemoteDBIngredientsResponse>
}
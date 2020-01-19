package com.education.cocktails.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailsDBApi {
    @GET("random.php")
    fun getRandomAsync()
            : Deferred<Response<TheCocktailsDBApiResponse>>

    @GET("search.php")
    fun searchByNameAsync(@Query("s") name: String)
            : Deferred<Response<TheCocktailsDBApiResponse>>

    @GET("search.php")
    fun searchByIngredientAsync(@Query("i") name: String)
            : Deferred<Response<TheCocktailsDBApiLiteResponse>>

    @GET("lookup.php")
    fun getFullDetailByIdAsync(@Query("i") id: Long)
            : Deferred<Response<TheCocktailsDBApiResponse>>

    //TODO check response by this get-request: may be LiteResponse
    @GET("filter.php")
    fun filterByCategoryAsync(@Query("c") category: String)
            : Deferred<Response<TheCocktailsDBApiLiteResponse>>
}
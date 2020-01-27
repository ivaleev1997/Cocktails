package com.education.cocktails.network

import com.education.cocktails.model.TheRemoteDBResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailsApi {
    @GET("random.php")
    suspend fun getRandomAsync()
            : Response<TheRemoteDBResponse>

    @GET("search.php")
    suspend fun searchByNameAsync(@Query("s") name: String)
            : Response<TheRemoteDBResponse>

    @GET("search.php")
    suspend fun searchByIngredientAsync(@Query("i") name: String)
            : Response<TheRemoteDBResponse>

    @GET("lookup.php")
    suspend fun getFullDetailByIdAsync(@Query("i") id: Long)
            : Response<TheRemoteDBResponse>

    //TODO check response by this get-request: may be LiteResponse
    @GET("filter.php")
    suspend fun filterByCategoryAsync(@Query("c") category: String)
            : Response<TheRemoteDBResponse>
}
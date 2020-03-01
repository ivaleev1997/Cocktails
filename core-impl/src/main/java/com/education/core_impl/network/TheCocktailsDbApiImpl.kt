package com.education.core_impl.network

import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.CocktailsJsonDeserializer
import com.education.core_api.network.TheCocktailsDbApi
import com.education.core_api.network.TheCocktailsDbApiContract
import com.education.core_impl.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TheCocktailsDbApiImpl : TheCocktailsDbApiContract {

    override fun getInstance(): TheCocktailsDbApi {
        return apiInstance
    }

    private val apiInstance: TheCocktailsDbApi by lazy {
        val okHttpBuilder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
        }
        val okHttpClient = okHttpBuilder.build()
        val gson = GsonBuilder().registerTypeAdapter(Cocktail::class.java, CocktailsJsonDeserializer).create()

        return@lazy Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .build()
            .create(TheCocktailsDbApi::class.java)
    }

}
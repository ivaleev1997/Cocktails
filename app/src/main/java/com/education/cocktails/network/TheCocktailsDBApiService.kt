package com.education.cocktails.network

import com.education.cocktails.BuildConfig
import com.education.cocktails.customPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    val apiService: TheCocktailsDBApi by lazy {
        val okHttpBuilder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
        }
        val okHttpClient = okHttpBuilder.build()
        val gson = GsonBuilder().setFieldNamingStrategy(customPolicy).create()

        return@lazy Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .build()
            .create(TheCocktailsDBApi::class.java)
    }
}
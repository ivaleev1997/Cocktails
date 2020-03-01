package com.education.core_api.network

interface NetworkProvider {

    //fun provideNetworkContract(): TheCocktailsDbApiContract

    fun provideTheCocktailsDbApi(): TheCocktailsDbApi
}
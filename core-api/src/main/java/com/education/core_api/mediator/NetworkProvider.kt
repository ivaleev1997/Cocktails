package com.education.core_api.mediator

import com.education.core_api.network.TheCocktailsDbApi

interface NetworkProvider {

    fun provideTheCocktailsDbApi(): TheCocktailsDbApi
}
package com.education.core_impl.network

import com.education.core_api.network.TheCocktailsDbApi
import com.education.core_api.network.TheCocktailsDbApiContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkContract(): TheCocktailsDbApiContract {
        return TheCocktailsDbApiImpl
    }

    @Provides
    @Singleton
    fun provideTheCocktailsDbApi(networkContract: TheCocktailsDbApiContract): TheCocktailsDbApi {
        return networkContract.getInstance()
    }
}
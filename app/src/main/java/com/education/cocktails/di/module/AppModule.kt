package com.education.cocktails.di.module

import android.app.Application
import androidx.room.Room
import com.education.cocktails.CocktailsRepository
import com.education.cocktails.db.CocktailsDb
import com.education.cocktails.network.Api
import com.education.cocktails.network.TheCocktailsApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideTheCocktailsAPi(): TheCocktailsApi {
        return Api.API_SERVICE
    }

    @Provides
    @Singleton
    fun provideCocktailsDb(application: Application): CocktailsDb {
        return Room
            .databaseBuilder(application, CocktailsDb::class.java, "cocktailsDb")
            .build()
    }

    @Provides
    @Singleton
    fun provideCocktailsRepository(cocktailsDb: CocktailsDb, theCocktailsApi: TheCocktailsApi): CocktailsRepository {
        return CocktailsRepository(cocktailsDb, theCocktailsApi)
    }

}
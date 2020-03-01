package com.education.core_impl.database

import android.content.Context
import androidx.room.Room
import com.education.core_api.database.CategoryDao
import com.education.core_api.database.CocktailDao
import com.education.core_api.database.CocktailsDataBaseContract
import com.education.core_api.database.IngredientDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideCocktailsDb(context: Context): CocktailsDataBaseContract {
        return Room
            .databaseBuilder(context, CocktailsDb::class.java, "cocktailsDb")
            .build()
    }

    @Provides
    @Reusable
    fun provideCategoryDao(dataBaseContract: CocktailsDataBaseContract): CategoryDao {
        return dataBaseContract.getCategoryDao()
    }

    @Provides
    @Reusable
    fun provideCocktailDao(dataBaseContract: CocktailsDataBaseContract): CocktailDao {
        return dataBaseContract.getCocktailDao()
    }

    @Provides
    @Reusable
    fun provideIngredientDao(dataBaseContract: CocktailsDataBaseContract): IngredientDao {
        return dataBaseContract.getIngredientDao()
    }
}
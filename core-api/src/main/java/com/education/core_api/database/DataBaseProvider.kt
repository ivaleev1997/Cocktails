package com.education.core_api.database

interface DataBaseProvider {

    //fun provideDataBaseContract(): CocktailsDataBaseContract

    fun provideCategoryDao(): CategoryDao

    fun provideCocktailDao(): CocktailDao

    fun provideIngredientDao(): IngredientDao
}
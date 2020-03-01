package com.education.core_api.database

interface DataBaseProvider {

    fun provideCategoryDao(): CategoryDao

    fun provideCocktailDao(): CocktailDao

    fun provideIngredientDao(): IngredientDao
}
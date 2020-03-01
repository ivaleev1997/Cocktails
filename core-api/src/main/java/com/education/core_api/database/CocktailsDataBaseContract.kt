package com.education.core_api.database

interface CocktailsDataBaseContract {

    fun getCategoryDao(): CategoryDao

    fun getCocktailDao(): CocktailDao

    fun getIngredientDao(): IngredientDao
}
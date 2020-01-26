package com.education.cocktails.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.CocktailsCategory
import com.education.cocktails.model.CocktailsIngredient

@Database(
    entities = [
        Cocktail::class,
        CocktailsCategory::class,
        CocktailsIngredient::class],
    version = 1,
    exportSchema = false
)
abstract class CocktailsDb: RoomDatabase() {
    abstract fun getCocktailDao(): CocktailDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getIngredientDao(): IngredientDao
}
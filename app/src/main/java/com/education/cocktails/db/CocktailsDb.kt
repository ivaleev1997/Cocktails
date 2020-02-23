package com.education.cocktails.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.education.cocktails.model.Category
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.Ingredient
import com.education.cocktails.util.Converters

@Database(
    entities = [
        Cocktail::class,
        Category::class,
        Ingredient::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CocktailsDb: RoomDatabase() {
    abstract fun getCocktailDao(): CocktailDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getIngredientDao(): IngredientDao
}
package com.education.cocktails.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.CocktailsCategory
import com.education.cocktails.model.CocktailsIngredient
import com.education.cocktails.util.Converters

@Database(
    entities = [
        Cocktail::class,
        CocktailsCategory::class,
        CocktailsIngredient::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CocktailsDb: RoomDatabase() {
    abstract fun getCocktailDao(): CocktailDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getIngredientDao(): IngredientDao
}
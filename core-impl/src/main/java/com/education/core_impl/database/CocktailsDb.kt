package com.education.core_impl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.education.core_api.database.CocktailsDataBaseContract
import com.education.core_api.dto.Category
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Ingredient
import com.education.core_api.dto.IngredientsConverter

@Database(
    entities = [
        Cocktail::class,
        Category::class,
        Ingredient::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(IngredientsConverter::class)
abstract class CocktailsDb : RoomDatabase(), CocktailsDataBaseContract
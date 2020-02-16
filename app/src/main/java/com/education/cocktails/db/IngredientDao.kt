package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.cocktails.model.CocktailsIngredient

@Dao
interface IngredientDao {

    @Query("SELECT * FROM cocktailsingredient")
    fun getIngredients(): LiveData<List<CocktailsIngredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredientList: List<CocktailsIngredient>)
}
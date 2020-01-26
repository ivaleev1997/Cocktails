package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.education.cocktails.model.CocktailsIngredient

@Dao
interface IngredientDao {

    @Query("SELECT * FROM cocktailsingredient")
    fun getIngredients(): LiveData<List<CocktailsIngredient>>
}
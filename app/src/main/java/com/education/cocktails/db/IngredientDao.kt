package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.cocktails.model.Ingredient

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredient")
    fun getIngredients(): LiveData<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredientList: List<Ingredient>)
}
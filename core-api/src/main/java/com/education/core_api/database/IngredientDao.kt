package com.education.core_api.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.core_api.dto.Ingredient

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredient")
    fun getIngredients(): LiveData<List<Ingredient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredientList: List<Ingredient>)
}
package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.education.cocktails.model.Cocktail

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    fun getCocktails(): LiveData<List<Cocktail>>

}
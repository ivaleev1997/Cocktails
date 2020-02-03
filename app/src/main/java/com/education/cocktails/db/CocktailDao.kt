package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.cocktails.model.Cocktail

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    fun getCocktails(): LiveData<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktails(cocktailList: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM cocktail WHERE :id = cocktail.idDrink")
    fun getCocktailById(id: Long): List<Cocktail>

    @Query("SELECT * FROM cocktail WHERE cocktail.favorite = 1")
    fun getFavoriteCocktails(): List<Cocktail>
}
package com.education.core_api.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.education.core_api.dto.Cocktail

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    fun getCocktails(): LiveData<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktails(cocktailList: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM cocktail WHERE :id = cocktail.idDrink")
    fun getCocktailById(id: Long): LiveData<List<Cocktail>>

    @Query("SELECT * FROM cocktail WHERE cocktail.favorite = 1")
    fun getFavoriteCocktails(): List<Cocktail>

    @Query("SELECT * FROM cocktail WHERE cocktail.idDrink IN (:listId)")
    fun getCocktailsByIds(listId: List<Long>): LiveData<List<Cocktail>>
}
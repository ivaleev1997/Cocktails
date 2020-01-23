package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.education.cocktails.model.CocktailsCategory

@Dao
interface CategoryDao {

    @Query("SELECT * FROM cocktail")
    fun getCategories(): LiveData<List<CocktailsCategory>>

}
package com.education.cocktails.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.education.cocktails.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>

}
package com.education.core_api.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.education.core_api.dto.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): LiveData<List<Category>>

}
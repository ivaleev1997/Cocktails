package com.education.core_api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("strCategory")
    val category: String
)
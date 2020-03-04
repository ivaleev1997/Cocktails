package com.education.core_api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("strIngredient1")
    val ingredient: String
)
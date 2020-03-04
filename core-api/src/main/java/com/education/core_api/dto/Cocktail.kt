package com.education.core_api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Cocktail(
    @PrimaryKey(autoGenerate = false)
    val idDrink: Long,
    @SerializedName("strDrink")
    val drink: String,
    @SerializedName("strDrinkAlternate")
    val drinkAlternate: String?,
    @SerializedName("strVideo")
    val video: String?,
    @SerializedName("strCategory")
    val category: String?,
    @SerializedName("strAlcoholic")
    val alcoholic: String?,
    @SerializedName("strInstructions")
    val instructions: String?,
    @SerializedName("strDrinkThumb")
    val image: String?,

    var favorite: Boolean,
    val ingredientWithMeasure: List<Pair<String, String>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cocktail

        if (idDrink != other.idDrink) return false
        if (drink != other.drink) return false
        if (instructions != other.instructions) return false
        if (image != other.image) return false
        if (favorite != other.favorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idDrink.hashCode()
        result = 31 * result + drink.hashCode()
        result = 31 * result + (instructions?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + favorite.hashCode()
        return result
    }
}
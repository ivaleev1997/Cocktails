package com.education.cocktails.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Cocktail(
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

/*    val ingredient1: String?,
    val ingredient2: String?,
    val ingredient3: String?,
    val ingredient4: String?,
    val ingredient5: String?,
    val ingredient6: String?,
    val ingredient7: String?,
    val ingredient8: String?,
    val ingredient9: String?,
    val ingredient10: String?,
    val ingredient11: String?,
    val ingredient12: String?,
    val ingredient13: String?,
    val ingredient14: String?,
    val ingredient15: String?,
    val measure1: String?,
    val measure2: String?,
    val measure3: String?,
    val measure4: String?,
    val measure5: String?,
    val measure6: String?,
    val measure7: String?,
    val measure8: String?,
    val measure9: String?,
    val measure10: String?,
    val measure11: String?,
    val measure12: String?,
    val measure13: String?,
    val measure14: String?,
    val measure15: String?*/
)

@Entity
data class CocktailsCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("strCategory")
    val category: String
)

@Entity
data class CocktailsIngredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("strIngredient1")
    val ingredient: String
)
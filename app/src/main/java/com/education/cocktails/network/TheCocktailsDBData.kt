package com.education.cocktails.network

import com.google.gson.annotations.SerializedName

interface CocktailsBase {
    val idDrink: Long
    val drink: String
    val image: String?
}

data class CocktailsLite(
    override val idDrink: Long,
    @SerializedName("strDrink")
    override val drink: String,
    @SerializedName("strDrinkThumb")
    override val image: String?
) : CocktailsBase
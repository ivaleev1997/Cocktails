package com.education.cocktails.network

import com.education.cocktails.model.Cocktail

data class TheCocktailsDBApiResponse(
    val drinks: List<Cocktail>
)

data class TheCocktailsDBApiLiteResponse(
    val drinks: List<Cocktail>
)
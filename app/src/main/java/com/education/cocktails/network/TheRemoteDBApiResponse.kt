package com.education.cocktails.network

import com.education.cocktails.model.CocktailDataEntity

data class TheCocktailsDBApiResponse(
    val drinks: List<CocktailDataEntity>
)

data class TheCocktailsDBApiLiteResponse(
    val drinks: List<CocktailsLite>
)
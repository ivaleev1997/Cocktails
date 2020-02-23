package com.education.cocktails.model

data class TheRemoteDBCocktailsResponse(
    val drinks: List<Cocktail>
)

data class TheRemoteDBIngredientsResponse(
    val drinks: List<Ingredient>
)
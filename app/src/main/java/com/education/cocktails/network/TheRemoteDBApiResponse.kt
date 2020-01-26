package com.education.cocktails.network

import com.education.cocktails.model.Cocktail

data class TheRemoteDBResponse(
    val drinks: List<Cocktail>
)
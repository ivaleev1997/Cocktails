package com.education.details.usecase

import androidx.lifecycle.LiveData
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Resource
import kotlinx.coroutines.CoroutineScope

interface DetailsUseCase {

    val favoriteStatus: LiveData<Boolean>

    fun loadCocktailsDetails(cocktailId: Long, coroutineScope: CoroutineScope)
            : LiveData<Resource<List<Cocktail>>>

    fun changeFavorite(coroutineScope: CoroutineScope)
}
package com.education.cocktails.usecase

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.repository.CocktailsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsUseCase
@Inject constructor(
    private val cocktailsRepository: CocktailsRepository
) {
    fun loadCocktailsDetails(cocktailId: Long, coroutineScope: CoroutineScope)
            : LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktailById(cocktailId, coroutineScope)
    }

    fun changeFavorite(cocktailsId: Long, flag: Boolean, coroutineScope: CoroutineScope) {
        cocktailsRepository.changeFavoriteFlag(cocktailsId, flag, coroutineScope)
    }
}
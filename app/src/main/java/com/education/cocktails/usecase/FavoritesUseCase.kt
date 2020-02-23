package com.education.cocktails.usecase

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.repository.CocktailsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesUseCase
@Inject constructor(private val cocktailsRepository: CocktailsRepository) {

    fun loadFavoriteCocktails(coroutineScope: CoroutineScope): LiveData<List<Cocktail>> {
        return cocktailsRepository.loadFavoriteCocktails(coroutineScope)
    }
}
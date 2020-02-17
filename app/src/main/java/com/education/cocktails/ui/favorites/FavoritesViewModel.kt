package com.education.cocktails.ui.favorites

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.ui.BaseViewModel
import javax.inject.Inject

class FavoritesViewModel
@Inject constructor(cocktailsRepository: CocktailsRepository): BaseViewModel(cocktailsRepository) {

    fun loadFavoriteCocktails(): LiveData<List<Cocktail>> {
        return cocktailsRepository.loadFavoriteCocktails(uiScope)
    }
}
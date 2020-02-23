package com.education.cocktails.ui.favorites

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.ui.BaseViewModel
import com.education.cocktails.usecase.FavoritesUseCase
import javax.inject.Inject

class FavoritesViewModel
@Inject constructor(private val favoritesUseCase: FavoritesUseCase): BaseViewModel() {

    fun loadFavoriteCocktails(): LiveData<List<Cocktail>> {
        return favoritesUseCase.loadFavoriteCocktails(uiScope)
    }
}
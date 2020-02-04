package com.education.cocktails.ui.mainlist

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.ui.BaseViewModel
import javax.inject.Inject

class CocktailsMainViewModel
    @Inject constructor(cocktailsRepository: CocktailsRepository): BaseViewModel(cocktailsRepository) {

    fun loadCocktails(): LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktails(uiScope)
    }
}
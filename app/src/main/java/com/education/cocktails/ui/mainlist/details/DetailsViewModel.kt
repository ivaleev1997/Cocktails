package com.education.cocktails.ui.mainlist.details

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.ui.BaseViewModel
import javax.inject.Inject

class DetailsViewModel
@Inject constructor(cocktailsRepository: CocktailsRepository): BaseViewModel(cocktailsRepository){

    var cocktailId: Long = 0L

    fun loadCocktailDetails(): LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktailById(cocktailId, uiScope)
    }

    fun setFavorite(flag: Boolean) {
        cocktailsRepository.changeFavoriteFlag(cocktailId, flag, uiScope)
    }
}
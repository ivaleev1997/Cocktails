package com.education.cocktails.ui.details

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.ui.BaseViewModel
import com.education.cocktails.usecase.DetailsUseCase
import javax.inject.Inject

class DetailsViewModel
@Inject constructor(
    private val detailsUseCase: DetailsUseCase
): BaseViewModel() {

    var cocktailId: Long = 0L

    fun loadCocktailDetails(): LiveData<Resource<List<Cocktail>>> {
        return detailsUseCase.loadCocktailsDetails(cocktailId, uiScope)
    }

    fun changeFavorite(flag: Boolean) {
        detailsUseCase.changeFavorite(cocktailId, flag, uiScope)
    }
}
package com.education.details.viewmodel

import androidx.lifecycle.LiveData
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Resource
import com.education.details.usecase.DetailsUseCase
import com.education.ui_core.BaseViewModel
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase
): BaseViewModel() {

    fun favoriteStatus(): LiveData<Boolean> {
        return detailsUseCase.favoriteStatus
    }

    fun loadCocktailDetails(cocktailId: Long): LiveData<Resource<List<Cocktail>>> {
        return detailsUseCase.loadCocktailsDetails(cocktailId, uiScope)
    }

    fun changeFavorite() {
        detailsUseCase.changeFavorite(uiScope)
    }
}

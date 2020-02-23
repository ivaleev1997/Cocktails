package com.education.cocktails.ui.mainlist

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.ui.BaseViewModel
import com.education.cocktails.usecase.CocktailsMainUseCase
import javax.inject.Inject

class CocktailsMainViewModel
    @Inject constructor(private val cocktailsMainUseCase: CocktailsMainUseCase): BaseViewModel() {

    fun loadCocktails(): LiveData<Resource<List<Cocktail>>> {
        return cocktailsMainUseCase.loadCocktails(uiScope)
    }
}
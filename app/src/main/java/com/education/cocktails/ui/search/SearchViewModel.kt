package com.education.cocktails.ui.search

import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.ui.BaseViewModel
import javax.inject.Inject

class SearchViewModel
@Inject constructor(cocktailsRepository: CocktailsRepository)
    : BaseViewModel(cocktailsRepository) {

    fun loadCocktailsBySearchRequest(request: String) {

    }
}
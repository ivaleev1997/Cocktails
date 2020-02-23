package com.education.cocktails.ui.search

import androidx.lifecycle.LiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.Suggestion
import com.education.cocktails.network.Resource
import com.education.cocktails.ui.BaseViewModel
import com.education.cocktails.usecase.SearchUseCase
import javax.inject.Inject

class SearchViewModel
@Inject constructor(private val searchUseCase: SearchUseCase)
    : BaseViewModel() {

    fun searchResults(): LiveData<Resource<List<Cocktail>>> {
        return searchUseCase.searchResultLiveData
    }

    fun loadSuggestions(): LiveData<List<Suggestion>> {
        searchUseCase.loadSuggestions(uiScope)

        return searchUseCase.suggestionResultLiveData
    }

    fun selectSuggestion(suggestion: Suggestion) {
        searchUseCase.searchBySuggestion(suggestion, uiScope)
    }

    fun selectQuery(query: String) {
        searchUseCase.searchByQuery(query, uiScope)
    }
}
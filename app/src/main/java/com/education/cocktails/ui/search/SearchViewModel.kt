package com.education.cocktails.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.education.cocktails.Suggestion
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.network.Status
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.ui.BaseViewModel
import javax.inject.Inject

class SearchViewModel
@Inject constructor(cocktailsRepository: CocktailsRepository)
    : BaseViewModel(cocktailsRepository) {

    companion object {
        const val COCKTAILS_TABLE = "cocktails"
        const val INGREDIENTS_TABLE = "ingredients"
    }

    private val _searchResultLiveData = MediatorLiveData<Resource<List<Cocktail>>>()

    val searchResultLiveData: LiveData<Resource<List<Cocktail>>>
        get() = _searchResultLiveData

    fun loadSuggestions(): LiveData<List<Suggestion>> {
        val resultLiveData = MediatorLiveData<List<Suggestion>>()
        val loadCocktails = cocktailsRepository.loadCocktails(uiScope)
        val loadIngredients = cocktailsRepository.loadIngredients(uiScope)

        resultLiveData.addSource(loadCocktails) { resourceCocktails ->
            if (!resourceCocktails.data.isNullOrEmpty()) {
                resultLiveData.removeSource(loadCocktails)
                val mutableList = (resourceCocktails.data.map { Suggestion(it.idDrink, it.drink, COCKTAILS_TABLE)}).toMutableList()
                resultLiveData.addSource(loadIngredients) { resourceIngredients ->

                    if (!resourceIngredients.data.isNullOrEmpty()) {
                        resultLiveData.removeSource(loadIngredients)
                        mutableList.addAll(resourceIngredients.data.map { Suggestion(it.id, it.ingredient, INGREDIENTS_TABLE)})
                        resultLiveData.value = mutableList
                    }
                }
            }
        }

        return resultLiveData
    }

    fun findSelected(suggestion: Suggestion) {
        when (suggestion.table) {
            COCKTAILS_TABLE -> {
                val loadCocktails = cocktailsRepository.loadCocktailById(suggestion.id, uiScope)
                _searchResultLiveData.addSource(loadCocktails) { resource ->
                    if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                        _searchResultLiveData.removeSource(loadCocktails)
                    }
                    _searchResultLiveData.value = resource
                }
            }
            INGREDIENTS_TABLE -> {
                val loadCocktailsByIngredients = cocktailsRepository.loadCocktailsByIngredientName(uiScope, suggestion.name.replace(" ", "_"))
                _searchResultLiveData.addSource(loadCocktailsByIngredients) { resource ->
                    if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                        _searchResultLiveData.removeSource(loadCocktailsByIngredients)
                    }
                    _searchResultLiveData.value = resource
                }
            }
        }
    }
}
package com.education.cocktails.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.Suggestion
import com.education.cocktails.network.Resource
import com.education.cocktails.network.Status
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.repository.IngredientsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUseCase
    @Inject constructor(
        private val cocktailsRepository: CocktailsRepository,
        private val ingredientsRepository: IngredientsRepository
    ) {

    companion object {
        const val COCKTAILS_TABLE = "cocktails"
        const val INGREDIENTS_TABLE = "ingredients"
    }

    private val _searchResultLiveData = MediatorLiveData<Resource<List<Cocktail>>>()
    val searchResultLiveData: LiveData<Resource<List<Cocktail>>>
        get() = _searchResultLiveData

    private val _suggestionResultLiveData = MediatorLiveData<List<Suggestion>>()
    val suggestionResultLiveData: LiveData<List<Suggestion>>
        get() = _suggestionResultLiveData

    fun loadSuggestions(coroutineScope: CoroutineScope) {
        val cocktailsSource = cocktailsRepository.loadCocktails(coroutineScope)
        val ingredientsSource = ingredientsRepository.loadIngredients(coroutineScope)

        _suggestionResultLiveData.addSource(cocktailsSource) { resourceCocktails ->
            if (!resourceCocktails.data.isNullOrEmpty()) {
                _suggestionResultLiveData.removeSource(cocktailsSource)
                val mutableList = (resourceCocktails.data.map {
                    Suggestion(
                        it.idDrink, it.drink,
                        COCKTAILS_TABLE
                    )
                }).toMutableList()

                _suggestionResultLiveData.addSource(ingredientsSource) { resourceIngredients ->
                    if (!resourceIngredients.data.isNullOrEmpty()) {
                        _suggestionResultLiveData.removeSource(ingredientsSource)
                        mutableList.addAll(resourceIngredients.data.map {
                            Suggestion(
                                it.id, it.ingredient,
                                INGREDIENTS_TABLE
                            )
                        })
                        _suggestionResultLiveData.value = mutableList.toSet().toList()
                    }
                }
            }
        }
    }

    fun searchBySuggestion(suggestion: Suggestion, coroutineScope: CoroutineScope) {
        when (suggestion.table) {
            COCKTAILS_TABLE -> {
                val loadCocktails = cocktailsRepository.loadCocktailById(suggestion.id, coroutineScope)
                _searchResultLiveData.addSource(loadCocktails) { resource ->
                    if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                        _searchResultLiveData.removeSource(loadCocktails)
                    }
                    _searchResultLiveData.value = resource
                }
            }
            INGREDIENTS_TABLE -> {
                val loadCocktailsByIngredients = cocktailsRepository.loadCocktailsByIngredientName(coroutineScope, suggestion.name.replace(" ", "_"))
                _searchResultLiveData.addSource(loadCocktailsByIngredients) { resource ->
                    if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                        _searchResultLiveData.removeSource(loadCocktailsByIngredients)
                    }
                    _searchResultLiveData.value = resource
                }
            }
        }
    }

    fun searchByQuery(query: String, coroutineScope: CoroutineScope) {
        val listSuggestions = _suggestionResultLiveData.value
        if (!listSuggestions.isNullOrEmpty()) {
            val filteredByQuery = listSuggestions.filter { it.name.contains(query, true) }
            val idsFromCocktails = filteredByQuery.filter { it.table == COCKTAILS_TABLE }.map { it.id }
            val namesFromIngredients = filteredByQuery.filter { it.table == INGREDIENTS_TABLE }.map { it.name }
            val loadCocktailsByIds = cocktailsRepository.loadCocktailsByIds(idsFromCocktails)
            val loadCocktailsByIngredientName = cocktailsRepository.loadCocktailsByIngredientNamesList(coroutineScope, namesFromIngredients)

            _searchResultLiveData.addSource(loadCocktailsByIds) { cocktailsList ->
                if (!cocktailsList.isNullOrEmpty()) {
                    _searchResultLiveData.removeSource(loadCocktailsByIds)
                    val mutableList = cocktailsList.toMutableList()
                    _searchResultLiveData.addSource(loadCocktailsByIngredientName) { resource ->
                        if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                            _searchResultLiveData.removeSource(loadCocktailsByIngredientName)
                            val fromResource = resource.data
                            if (!fromResource.isNullOrEmpty()) {
                                mutableList += resource.data
                                _searchResultLiveData.value = Resource.success(mutableList.toSet().toList())
                            }
                        }
                    }
                }
            }
        }
    }
}
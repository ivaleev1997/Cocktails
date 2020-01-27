package com.education.cocktails.ui.mainlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.education.cocktails.repository.CocktailsRepository
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class CocktailsMainFragmentViewModel
    @Inject constructor(val cocktailsRepository: CocktailsRepository): ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        cocktailsRepository.currentCoroutineScope = uiScope
    }

    fun loadCocktails(): LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktails()
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}
package com.education.cocktails.ui.mainlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.repository.CocktailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class CocktailsMainFragmentViewModel
    @Inject constructor(private val cocktailsRepository: CocktailsRepository): ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var cocktailId: Long = 0L

    init {
        cocktailsRepository.currentCoroutineScope = uiScope
    }

    fun loadCocktails(): LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktails()
    }

    fun loadCocktailDetails():LiveData<Resource<List<Cocktail>>> {
        return cocktailsRepository.loadCocktailById(cocktailId)
    }

    fun setFavorite(flag: Boolean) {
        cocktailsRepository.changeFavoriteFlag(cocktailId, flag)
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}
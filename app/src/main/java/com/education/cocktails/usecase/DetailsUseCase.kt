package com.education.cocktails.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Resource
import com.education.cocktails.network.Status
import com.education.cocktails.repository.CocktailsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class DetailsUseCase
@Inject constructor(
    private val cocktailsRepository: CocktailsRepository
) {
    private lateinit var cocktail: Cocktail
    private val _favoriteStatus = MutableLiveData<Boolean>()
    val favoriteStatus: LiveData<Boolean>
        get() = _favoriteStatus

    fun loadCocktailsDetails(cocktailId: Long, coroutineScope: CoroutineScope)
            : LiveData<Resource<List<Cocktail>>> {
        val mediatorLiveData = MediatorLiveData<Resource<List<Cocktail>>>()
        mediatorLiveData.addSource(cocktailsRepository.loadCocktailById(cocktailId, coroutineScope)) { resource ->
            if ((resource.status == Status.SUCCESS || resource.status == Status.ERROR) && !resource.data.isNullOrEmpty()) {
                cocktail = resource.data[0]
                mediatorLiveData.value = resource
                _favoriteStatus.value = cocktail.favorite
            }
        }

        return mediatorLiveData
    }

    fun changeFavorite(coroutineScope: CoroutineScope) {
        cocktail.favorite = !cocktail.favorite
        cocktailsRepository.saveCocktail(cocktail, coroutineScope)
        //cocktailsRepository.changeFavoriteFlag(cocktailsId, cocktail.favorite, coroutineScope)
    }
}
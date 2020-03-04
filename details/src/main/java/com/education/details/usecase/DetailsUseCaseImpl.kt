package com.education.details.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Resource
import com.education.core_api.dto.Status
import com.education.details.repository.DetailsRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DetailsUseCaseImpl
@Inject constructor(
    private val detailsRepository: DetailsRepository
) : DetailsUseCase {
    private lateinit var cocktail: Cocktail
    private val _favoriteStatus = MutableLiveData<Boolean>()
    override val favoriteStatus: LiveData<Boolean>
        get() = _favoriteStatus

    override fun loadCocktailsDetails(cocktailId: Long, coroutineScope: CoroutineScope)
            : LiveData<Resource<List<Cocktail>>> {
        val mediatorLiveData = MediatorLiveData<Resource<List<Cocktail>>>()
        mediatorLiveData.addSource(detailsRepository.loadCocktailById(cocktailId, coroutineScope)) { resource ->
            if ((resource.status == Status.SUCCESS || resource.status == Status.ERROR) && !resource.data.isNullOrEmpty()) {
                cocktail = resource.data!!.first()
                mediatorLiveData.value = resource
                _favoriteStatus.value = cocktail.favorite
            }
        }

        return mediatorLiveData
    }

    override fun changeFavorite(coroutineScope: CoroutineScope) {
        cocktail.favorite = !cocktail.favorite
        detailsRepository.saveCocktail(cocktail, coroutineScope)
        //cocktailsRepository.changeFavoriteFlag(cocktailsId, cocktail.favorite, coroutineScope)
    }


}
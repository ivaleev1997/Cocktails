package com.education.details.repository

import androidx.lifecycle.LiveData
import com.education.core_api.dto.Cocktail
import com.education.core_api.dto.Resource
import kotlinx.coroutines.CoroutineScope

interface DetailsRepository {

    fun loadCocktailById(id: Long, coroutineScope: CoroutineScope): LiveData<Resource<List<Cocktail>>>

    fun saveCocktail(cocktail: Cocktail, coroutineScope: CoroutineScope)
}
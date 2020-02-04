package com.education.cocktails.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.education.cocktails.APP_TAG
import com.education.cocktails.repository.CocktailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel(val cocktailsRepository: CocktailsRepository) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
        Log.d(APP_TAG, "BaseViewModel onCleared")
    }
}
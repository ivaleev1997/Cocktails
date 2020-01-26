package com.education.cocktails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class AppViewModelFactory
    @Inject constructor(private val creators: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val found = creators.entries.single {
            modelClass.isAssignableFrom(it.key)
        }

        return found.value.get() as T
    }
}
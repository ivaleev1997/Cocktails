package com.education.cocktails.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.CocktailsMainFragmentViewModel
import com.education.cocktails.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindAppViewModelFactory(factory: AppViewModelFactory?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(CocktailsMainFragmentViewModel::class)
    abstract fun bindCocktailsListFragmentViewModel(cocktailsMainFragmentViewModel: CocktailsMainFragmentViewModel)
            : ViewModel
}
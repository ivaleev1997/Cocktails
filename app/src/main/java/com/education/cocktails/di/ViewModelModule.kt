package com.education.cocktails.di

import androidx.lifecycle.ViewModel
import com.education.cocktails.CocktailsListFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    abstract fun bindCocktailsListFragmentViewModel(cocktailsListFragmentViewModel: CocktailsListFragmentViewModel)
            : ViewModel
}
package com.education.cocktails.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.di.ViewModelKey
import com.education.cocktails.ui.mainlist.CocktailsMainViewModel
import com.education.cocktails.ui.mainlist.details.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindAppViewModelFactory(factory: AppViewModelFactory?): ViewModelProvider.Factory?

    @Binds
    @IntoMap
    @ViewModelKey(CocktailsMainViewModel::class)
    abstract fun bindCocktailsMainViewModel(cocktailsMainViewModel: CocktailsMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel
}
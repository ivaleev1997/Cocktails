package com.education.cocktails.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.di.ViewModelKey
import com.education.cocktails.ui.details.DetailsViewModel
import com.education.cocktails.ui.favorites.FavoritesViewModel
import com.education.cocktails.ui.mainlist.CocktailsMainViewModel
import com.education.cocktails.ui.search.SearchViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel):ViewModel
}
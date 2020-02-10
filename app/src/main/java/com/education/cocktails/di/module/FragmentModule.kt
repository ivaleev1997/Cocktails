package com.education.cocktails.di.module

import com.education.cocktails.ui.details.DetailsFragment
import com.education.cocktails.ui.favorites.FavoritesFragmentShared
import com.education.cocktails.ui.mainlist.CocktailsMainFragmentShared
import com.education.cocktails.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCocktailsMainFragment(): CocktailsMainFragmentShared

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesFragment(): FavoritesFragmentShared

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
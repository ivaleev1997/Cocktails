package com.education.cocktails.di.module

import com.education.cocktails.ui.details.DetailsFragment
import com.education.cocktails.ui.favorites.FavoritesFragment
import com.education.cocktails.ui.mainlist.CocktailsMainFragment
import com.education.cocktails.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCocktailsMainFragment(): CocktailsMainFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
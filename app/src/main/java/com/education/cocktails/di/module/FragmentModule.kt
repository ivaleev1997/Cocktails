package com.education.cocktails.di.module

import com.education.cocktails.ui.mainlist.CocktailsMainFragment
import com.education.cocktails.ui.mainlist.details.DetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCocktailsMainFragment(): CocktailsMainFragment

    @ContributesAndroidInjector
    abstract fun contributeCocktailDetailsFragment(): DetailsFragment
}
package com.education.cocktails.di.module

import com.education.cocktails.ui.mainlist.CocktailsMainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCocktailsMainFragment(): CocktailsMainFragment
}
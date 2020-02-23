package com.education.cocktails.di

import android.app.Application
import com.education.cocktails.App
import com.education.cocktails.di.module.AppModule
import com.education.cocktails.di.module.MainActivityModule
import com.education.cocktails.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    MainActivityModule::class,
    ViewModelModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<App>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
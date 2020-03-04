package com.education.details.di

import com.education.core.CoreCocktailsComponent
import com.education.core_api.mediator.ProvidersFacade
import com.education.core_api.viewmodel.ViewModelsProvider
import com.education.details.DetailsFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [DetailsModule::class],
    dependencies = [ProvidersFacade::class, ViewModelsProvider::class]
)
@Singleton
interface DetailsComponent {

    companion object {
        fun create(providersFacade: ProvidersFacade): DetailsComponent {
            return DaggerDetailsComponent
                .builder()
                .providersFacade(providersFacade)
                .viewModelsProvider(CoreCocktailsComponent.createViewModelComponent())
                .build()
        }
    }

    fun inject(detailsFragment: DetailsFragment)
}
package com.education.core

import com.education.core_api.database.DataBaseProvider
import com.education.core_api.mediator.AppProvider
import com.education.core_api.network.NetworkProvider
import com.education.core_api.viewmodel.ViewModelsProvider
import com.education.core_impl.database.DaggerDataBaseComponent
import com.education.core_impl.network.DaggerNetworkComponent
import com.education.core_impl.viewmodel.DaggerViewModelComponent

object CoreCocktailsComponent {

    fun createViewModelComponent(): ViewModelsProvider {
        return DaggerViewModelComponent.create()
    }

    fun createDataBaseComponent(appProvider: AppProvider): DataBaseProvider {
        return DaggerDataBaseComponent.builder().appProvider(appProvider).build()
    }

    fun createNetworkComponent(): NetworkProvider {
        return DaggerNetworkComponent.create()
    }
}
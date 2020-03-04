package com.education.core_impl.viewmodel

import com.education.core_api.viewmodel.ViewModelsProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [ViewModelModule::class]
)
@Singleton
interface ViewModelComponent : ViewModelsProvider
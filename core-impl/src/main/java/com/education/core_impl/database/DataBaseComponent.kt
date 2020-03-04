package com.education.core_impl.database

import com.education.core_api.database.DataBaseProvider
import com.education.core_api.mediator.AppProvider
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [DataBaseModule::class],
    dependencies = [AppProvider::class]
)
@Singleton
interface DataBaseComponent: DataBaseProvider
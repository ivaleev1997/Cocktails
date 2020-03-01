package com.education.core_impl.database

import com.education.core_api.database.DataBaseProvider
import com.education.core_api.mediator.AppProvider
import dagger.Component

@Component(
    modules = [DataBaseModule::class],
    dependencies = [AppProvider::class]
)
interface DataBaseComponent: DataBaseProvider
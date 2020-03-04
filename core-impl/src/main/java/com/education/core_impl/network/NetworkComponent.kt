package com.education.core_impl.network

import com.education.core_api.network.NetworkProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [NetworkModule::class]
)
@Singleton
interface NetworkComponent: NetworkProvider
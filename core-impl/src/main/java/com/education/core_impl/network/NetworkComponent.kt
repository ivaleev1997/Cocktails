package com.education.core_impl.network

import com.education.core_api.network.NetworkProvider
import dagger.Component

@Component(
    modules = [NetworkModule::class]
)
interface NetworkComponent: NetworkProvider
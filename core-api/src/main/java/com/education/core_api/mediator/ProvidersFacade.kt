package com.education.core_api.mediator

import com.education.core_api.database.DataBaseProvider
import com.education.core_api.network.NetworkProvider

interface ProvidersFacade: AppProvider, DataBaseProvider,
    NetworkProvider
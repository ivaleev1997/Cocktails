package com.education.core_api.mediator

import com.education.core_api.database.DataBaseProvider

interface ProvidersFacade: AppProvider, DataBaseProvider, NetworkProvider
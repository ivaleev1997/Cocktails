package com.education.core_api.mediator

import android.content.Context

interface AppProvider {

    fun provideApp(): Context
}
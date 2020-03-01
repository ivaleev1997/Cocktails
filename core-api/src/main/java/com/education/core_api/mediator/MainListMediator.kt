package com.education.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface MainListMediator {

    fun startMainListScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}
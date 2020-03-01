package com.education.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface DetailsMediator {

    fun startDetailsScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}
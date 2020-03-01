package com.education.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface SearchMediator {

    fun startSearchScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}
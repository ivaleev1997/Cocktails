package com.education.core_api.mediator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface FavoritesMediator {

    fun startFavoritesScreen(@IdRes containerId: Int, fragmentManager: FragmentManager)
}
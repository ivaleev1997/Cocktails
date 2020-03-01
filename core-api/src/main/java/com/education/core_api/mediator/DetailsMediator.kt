package com.education.core_api.mediator

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.education.core_api.dto.Cocktail

interface DetailsMediator {

    fun startDetailsScreen(
        @IdRes containerId: Int,
        fragmentManager: FragmentManager,
        sharedView: View,
        cocktail: Cocktail,
        exitTransition: () -> Unit = {})
}
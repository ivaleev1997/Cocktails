package com.education.details

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import com.education.core_api.dto.Cocktail
import com.education.core_api.mediator.DetailsMediator
import com.education.ui_core.DetailsTransition

class DetailsMediatorImpl : DetailsMediator {
    override fun startDetailsScreen(
        containerId: Int,
        fragmentManager: FragmentManager,
        sharedView: View,
        cocktail: Cocktail,
        exitTransition: () -> Unit
    ) {
        val fragmentTransaction = fragmentManager.beginTransaction()
    }

    private fun setupTransition(fragment: Fragment, exitTransitionCall: () -> Unit) {
        val detailsTransition = DetailsTransition()
        fragment.sharedElementEnterTransition = detailsTransition
        fragment.enterTransition = Fade()
        exitTransitionCall()
        fragment.sharedElementReturnTransition = detailsTransition
    }
}
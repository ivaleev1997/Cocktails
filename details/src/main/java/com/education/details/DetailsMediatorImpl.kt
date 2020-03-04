package com.education.details

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import com.education.core_api.dto.Cocktail
import com.education.core_api.mediator.DetailsMediator
import com.education.ui_core.DetailsTransition
import javax.inject.Inject

class DetailsMediatorImpl
    @Inject constructor() : DetailsMediator {

    companion object {
        private const val sharedName = "cocktailTransition"
    }

    override fun startDetailsScreen(
        containerId: Int,
        fragmentManager: FragmentManager,
        sharedView: View,
        cocktail: Cocktail,
        exitTransition: () -> Unit
    ) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = DetailsFragment.getInstance(cocktail.idDrink, cocktail.image)
        setupTransition(fragment, exitTransition)
        fragmentTransaction.addSharedElement(sharedView, sharedName)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setupTransition(fragment: Fragment, exitTransitionCall: () -> Unit) {
        val detailsTransition = DetailsTransition()
        fragment.sharedElementEnterTransition = detailsTransition
        fragment.enterTransition = Fade()
        exitTransitionCall()
        fragment.sharedElementReturnTransition = detailsTransition
    }
}
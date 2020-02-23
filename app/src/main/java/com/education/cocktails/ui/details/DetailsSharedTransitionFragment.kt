package com.education.cocktails.ui.details

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.Fade
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import dagger.android.support.DaggerFragment

open class DetailsSharedTransitionFragment: DaggerFragment() {
    companion object {
        private const val sharedName = "cocktailTransition"
        fun startTransition(
            fragmentActivity: FragmentActivity?,
            cocktail: Cocktail,
            imageView: ImageView,
            exitTransitionCall: () -> Unit
        ) {
            val fragment = DetailsFragment.getInstance(cocktail.idDrink, cocktail.image)
            val fragmentManager = fragmentActivity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            if (fragmentTransaction != null) {
                setupTransition(fragment, exitTransitionCall)
                fragmentTransaction.addSharedElement(imageView, sharedName)
                fragmentTransaction.replace(R.id.fragment_container, fragment, "details")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        private fun setupTransition(fragment: Fragment, exitTransitionCall: () -> Unit) {
            val detailsTransition = DetailsTransition()
            fragment.sharedElementEnterTransition = detailsTransition
            fragment.enterTransition = Fade()
            exitTransitionCall()
            fragment.sharedElementReturnTransition = detailsTransition
        }
    }
}
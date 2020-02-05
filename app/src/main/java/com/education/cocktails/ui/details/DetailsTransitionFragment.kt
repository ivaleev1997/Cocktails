package com.education.cocktails.ui.details

import androidx.fragment.app.FragmentActivity
import com.education.cocktails.R
import dagger.android.support.DaggerFragment

open class DetailsTransitionFragment: DaggerFragment() {
    companion object {
        fun startTransition(fragmentActivity: FragmentActivity?, idDrink: Long) {
            val fragment = DetailsFragment.getInstance(idDrink)
            val fragmentManager = fragmentActivity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, "details")
                fragmentTransaction.addToBackStack("Details")
                fragmentTransaction.commit()
            }
        }
    }
}
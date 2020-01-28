package com.education.cocktails.ui.mainlist.details

import dagger.android.support.DaggerFragment

class DetailsFragment : DaggerFragment() {

    companion object {
        fun getInstance(): DetailsFragment {
            return DetailsFragment()
        }
    }
}
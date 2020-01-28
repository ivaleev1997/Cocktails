package com.education.cocktails.ui.favorites

import dagger.android.support.DaggerFragment

class FavoritesFragment: DaggerFragment() {
    companion object {
        fun getInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}
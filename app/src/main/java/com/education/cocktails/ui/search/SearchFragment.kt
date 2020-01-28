package com.education.cocktails.ui.search

import dagger.android.support.DaggerFragment

class SearchFragment : DaggerFragment() {

    companion object {
        fun getInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}
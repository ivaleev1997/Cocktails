package com.education.cocktails

import android.os.Bundle
import com.education.cocktails.ui.CocktailsMainFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, CocktailsMainFragment.newInstance())
        fragmentTransaction.commit()
    }
}

package com.education.cocktails

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.education.cocktails.ui.favorites.FavoritesFragment
import com.education.cocktails.ui.mainlist.CocktailsMainFragment
import com.education.cocktails.ui.search.SearchFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_main -> {
                    startMainFragment(CocktailsMainFragment.newInstance())
                    true
                }

                R.id.navigation_favorites -> {
                    startMainFragment(FavoritesFragment.getInstance())
                    true
                }

                R.id.navigation_search -> {
                    startMainFragment(SearchFragment.getInstance())
                    true
                }

                else -> false
            }
        }
        mainBottomNavigation.selectedItemId = R.id.navigation_main
    }

    private fun startMainFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}

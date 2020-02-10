package com.education.cocktails.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.GRID_LAYOUT_SPAN_COUNT
import com.education.cocktails.R
import com.education.cocktails.ui.details.DetailsSharedTransitionFragment
import com.education.cocktails.ui.mainlist.CocktailsAdapter
import javax.inject.Inject

class FavoritesFragmentShared: DetailsSharedTransitionFragment() {
    companion object {
        fun getInstance(): FavoritesFragmentShared {
            return FavoritesFragmentShared()
        }
    }

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private val favoritesViewModel:FavoritesViewModel by viewModels{
        appViewModelFactory
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CocktailsAdapter
    private lateinit var noFavoritesTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cocktails_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noFavoritesTextView = view.findViewById(R.id.no_favorite_textview)
        recyclerView = view.findViewById(R.id.cocktails_list_recycler)
        recyclerView.layoutManager = GridLayoutManager(context, GRID_LAYOUT_SPAN_COUNT)
        adapter = CocktailsAdapter { cocktail, image ->
            startTransition(activity, cocktail, image) {
                exitTransition = Fade()
            }
        }
        recyclerView.adapter = adapter

        favoritesViewModel.loadFavoriteCocktails().observe(viewLifecycleOwner) { favorites ->
            if (favorites.isEmpty()) {
                noFavoritesTextView.visibility = View.VISIBLE
            } else {
                adapter.cocktailsList = favorites
                adapter.notifyDataSetChanged()
                recyclerView.visibility = View.VISIBLE
            }
        }
    }
}
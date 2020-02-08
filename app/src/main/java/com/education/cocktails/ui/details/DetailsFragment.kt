package com.education.cocktails.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.cocktails.APP_TAG
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.R
import com.education.cocktails.network.Status
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailsFragment : DaggerFragment() {

    companion object {
        private const val BUNDLE_KEY = "Cocktail_id"

        fun getInstance(id: Long): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putLong(BUNDLE_KEY, id)
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private val detailsViewModel: DetailsViewModel by viewModels {
        appViewModelFactory
    }

    private lateinit var nameDrinkTextView: TextView
    private lateinit var imageDrink: ImageView
    private lateinit var instructionsTextView: TextView
    private lateinit var alcoholStatusTextView: TextView
    private lateinit var favoriteImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var appBar: AppBarLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private var currentTitle: String = " "
    private val adapter: IngredientsAdapter = IngredientsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cocktail_details, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.details_toolbar)
        appBar = view.findViewById(R.id.details_appbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar)
        setAppBarListener()
        if(activity is AppCompatActivity) {
            val act = activity as AppCompatActivity
            act.setSupportActionBar(toolbar)
            setHasOptionsMenu(true)
            act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            act.supportActionBar?.title = " "
        }

        return view
    }

    private fun setAppBarListener() {
        var isShow = true
        var scrollRange = -1
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                collapsingToolbarLayout.title = currentTitle
                isShow = true
            } else if (isShow){
                collapsingToolbarLayout.title = " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameDrinkTextView = view.findViewById(R.id.detail_name)
        instructionsTextView = view.findViewById(R.id.details_textview)
        alcoholStatusTextView = view.findViewById(R.id.alcohol_textview)
        imageDrink = view.findViewById(R.id.details_image)
        favoriteImageView = view.findViewById(R.id.favorite_image)
        recyclerView = view.findViewById(R.id.ingredients_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        detailsViewModel.cocktailId = getCocktailIdFromArguments()

        detailsViewModel
            .loadCocktailDetails()
            .observe(viewLifecycleOwner) { resource ->
                if (resource.status == Status.SUCCESS && !resource.data.isNullOrEmpty()) {
                    Glide.with(this).load(resource.data[0].image).into(imageDrink)
                    nameDrinkTextView.text = resource.data[0].drink
                    alcoholStatusTextView.text = resource.data[0].alcoholic
                    instructionsTextView.text = resource.data[0].instructions
                    adapter.ingredientsWithMeasure = resource.data[0].ingredientWithMeasure
                    adapter.notifyDataSetChanged()
                    currentTitle = resource.data[0].drink
                    setFavoriteImageView(resource.data[0].favorite)
                }
            }

        favoriteImageView.setOnClickListener {
            if (it.tag == getString(R.string.not_clicked_tag)) {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_clicked)
                it.tag = getString(R.string.clicked_tag)
                detailsViewModel.setFavorite(true)
            }
            else {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_not_clicked)
                it.tag = getString(R.string.not_clicked_tag)
                detailsViewModel.setFavorite(false)
            }
        }
    }

    private fun setFavoriteImageView(flag: Boolean) {
        if (flag) {
            favoriteImageView.setImageResource(R.drawable.ic_favorite_clicked)
            favoriteImageView.tag = R.string.clicked_tag
        }
    }

    private fun getCocktailIdFromArguments(): Long = arguments?.getLong(BUNDLE_KEY) ?: 0L

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(APP_TAG, "onOptionsItemSelected")

        return true
    }
}
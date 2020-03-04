package com.education.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.education.core_api.dto.Status
import com.education.details.di.ViewModelTrigger
import com.education.details.view.IngredientsAdapter
import com.education.details.viewmodel.DetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import javax.inject.Inject


class DetailsFragment : Fragment() {

    companion object {
        private const val COCKTAIL_ID = "Cocktail_id"
        private const val COCKTAIL_IMAGE_URL = "Cocktail_url"

        fun getInstance(id: Long, imageUrl: String?): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putLong(COCKTAIL_ID, id)
            bundle.putString(COCKTAIL_IMAGE_URL, imageUrl)
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject
    lateinit var appViewModelFactory: ViewModelProvider.Factory

    //this injection triggers to create detailsViewModel instance in ViewModelProvider.Factory
    @Inject
    lateinit var viewModelTrigger: ViewModelTrigger

    private val detailsViewModel: DetailsViewModel by viewModels {
        appViewModelFactory
    }

    private lateinit var nameDrinkTextView: TextView
    private lateinit var imageDrink: ImageView
    private lateinit var instructionsTextView: TextView
    private lateinit var alcoholStatusTextView: TextView
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
        imageDrink = view.findViewById(R.id.details_image)
        imageDrink.loadImage(getCocktailsImageUrl()) {
            startPostponedEnterTransition()
        }
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
        postponeEnterTransition()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)

        val favoriteItem = menu.findItem(R.id.favorite_item)

        detailsViewModel.favoriteStatus().observe(viewLifecycleOwner) { flag ->
            if (flag) favoriteItem.setIcon(R.drawable.ic_favorite_clicked)
            else favoriteItem.setIcon(R.drawable.ic_favorite_not_clicked)
        }
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
        recyclerView = view.findViewById(R.id.ingredients_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter

        detailsViewModel
            .loadCocktailDetails(getCocktailIdFromArguments())
            .observe(viewLifecycleOwner) { resource ->
                if (resource.status == Status.SUCCESS && !resource.data.isNullOrEmpty()) {
                    val cocktail = resource.data!![0]
                    nameDrinkTextView.text = cocktail.drink
                    alcoholStatusTextView.text = cocktail.alcoholic
                    instructionsTextView.text = cocktail.instructions
                    adapter.ingredientsWithMeasure = cocktail.ingredientWithMeasure
                    adapter.notifyDataSetChanged()
                    currentTitle = cocktail.drink
                    //setFavoriteImageView(resource.data[0].favorite)
                }
            }
    }

    private fun ImageView.loadImage(url: String, onLoadingFinished: () -> Unit) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }
        }
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.centerCropTransform().dontTransform().onlyRetrieveFromCache(true))
            .listener(listener)
            .into(this)
    }

    private fun getCocktailIdFromArguments(): Long = arguments?.getLong(COCKTAIL_ID) ?: 0L
    private fun getCocktailsImageUrl(): String = arguments?.getString(COCKTAIL_IMAGE_URL) ?: ""

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_item -> {
                detailsViewModel.changeFavorite()
            }
        }

        return true
    }
}

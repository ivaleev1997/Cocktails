package com.education.cocktails.ui.mainlist.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.R
import com.education.cocktails.network.Status
import com.education.cocktails.ui.mainlist.CocktailsMainFragmentViewModel
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

    private val cocktailsMainFragmentViewModel: CocktailsMainFragmentViewModel by viewModels {
        appViewModelFactory
    }

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cocktail_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = view.findViewById(R.id.details_textview)
        imageView = view.findViewById(R.id.details_image)

        val cocktailId = getCocktailIdFromArguments()
        if (cocktailId != null)
            cocktailsMainFragmentViewModel
                .loadCocktailDetails(cocktailId)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource.status == Status.SUCCESS && !resource.data.isNullOrEmpty()) {
                        Glide.with(this).load(resource.data[0].image).into(imageView)
                        textView.text = resource.data[0].instructions
                    }
                }
        else {
            TODO("not implemented")//show empty results
        }
    }

    private fun getCocktailIdFromArguments(): Long? = arguments?.getLong(BUNDLE_KEY)
}
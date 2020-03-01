package com.education.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders


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

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

package com.education.cocktails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.CocktailsAdapter
import com.education.cocktails.CocktailsMainFragmentViewModel
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Status
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CocktailsMainFragment: DaggerFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cocktailsAdapter: CocktailsAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance(): CocktailsMainFragment {
            return CocktailsMainFragment()
        }
    }

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private val cocktailsMainFragmentViewModel: CocktailsMainFragmentViewModel by viewModels {
        appViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cocktails_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.load_progressBar)
        recyclerView = view.findViewById(R.id.cocktails_main_recycler)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        cocktailsAdapter = CocktailsAdapter()
        recyclerView.adapter = cocktailsAdapter

        cocktailsMainFragmentViewModel.loadCocktails().observe(viewLifecycleOwner) {
            resource ->
            fun setData(cocktails: List<Cocktail>?): Boolean {
                var result = false
                if (!cocktails.isNullOrEmpty()) {
                    cocktailsAdapter.cocktailsList = cocktails
                    //TODO add diffutil
                    cocktailsAdapter.notifyDataSetChanged()

                    if (recyclerView.visibility == View.GONE) {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                    result = true
                }

                return result
            }
            when(resource.status) {
                Status.ERROR -> {
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }

                Status.SUCCESS -> setData(resource.data)

                Status.LOADING ->
                    if (!setData(resource.data)) {
                        Toast.makeText(context, "Loading data..", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
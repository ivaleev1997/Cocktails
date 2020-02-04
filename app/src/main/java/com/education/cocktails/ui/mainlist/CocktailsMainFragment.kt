package com.education.cocktails.ui.mainlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.cocktails.APP_TAG
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Status
import com.education.cocktails.ui.mainlist.details.DetailsFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CocktailsMainFragment: DaggerFragment(), CocktailsAdapter.DetailsCallback {
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

    private val cocktailsMainViewModel: CocktailsMainViewModel by viewModels {
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
        cocktailsAdapter = CocktailsAdapter(this)
        recyclerView.adapter = cocktailsAdapter

        cocktailsMainViewModel.loadCocktails().observe(viewLifecycleOwner) {
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

    override fun onSelected(id: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(APP_TAG, "onSelected id: $id")

        val fragment = DetailsFragment.getInstance(id)
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.fragment_container, fragment, "details")
            fragmentTransaction.addToBackStack("Details")
            fragmentTransaction.commit()
        }
    }
}
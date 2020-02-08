package com.education.cocktails.ui.mainlist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.cocktails.APP_TAG
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.GRID_LAYOUT_SPAN_COUNT
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Status
import com.education.cocktails.ui.details.DetailsTransitionFragment
import javax.inject.Inject

class CocktailsMainFragment: DetailsTransitionFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cocktailsAdapter: CocktailsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar

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
        val view = inflater.inflate(R.layout.fragment_cocktails_main, container, false)
        toolbar = view.findViewById(R.id.main_toolbar)

        (this.activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.load_progressBar)
        recyclerView = view.findViewById(R.id.cocktails_list_recycler)
        recyclerView.layoutManager = GridLayoutManager(context, GRID_LAYOUT_SPAN_COUNT)
        cocktailsAdapter = CocktailsAdapter { idDrink ->
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            Log.d(APP_TAG, "onSelected id: $id")
            startTransition(activity, idDrink)
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.alcohol_menu -> {
                item.isChecked = true

                true
            }
            R.id.no_alcohol_menu -> {
                item.isChecked = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
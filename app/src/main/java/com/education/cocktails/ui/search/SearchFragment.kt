package com.education.cocktails.ui.search

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.GRID_LAYOUT_SPAN_COUNT
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Status
import com.education.cocktails.ui.details.DetailsSharedTransitionFragment
import com.education.cocktails.ui.mainlist.CocktailsAdapter
import javax.inject.Inject

class SearchFragment : DetailsSharedTransitionFragment() {

    companion object {
        fun getInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels {
        appViewModelFactory
    }

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CocktailsAdapter
    private lateinit var searchViewWithSuggestions: SearchViewWithSuggestions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cocktails_search, container, false)
        toolbar = view.findViewById(R.id.search_toolbar)

        if(activity is AppCompatActivity) {
            val act = activity as AppCompatActivity
            act.setSupportActionBar(toolbar)
            setHasOptionsMenu(true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.cocktails_list_recycler)
        recyclerView.layoutManager = GridLayoutManager(context, GRID_LAYOUT_SPAN_COUNT)
        adapter = CocktailsAdapter { cocktail, imageView ->
            startTransition(activity, cocktail, imageView) {
                exitTransition = Fade()
            }
        }
        recyclerView.adapter = adapter

        searchViewModel.searchResults().observe(viewLifecycleOwner) { resource ->
            fun setData(cocktails: List<Cocktail>?) {
                if (!cocktails.isNullOrEmpty()) {
                    // TODO diffUtil
                    adapter.cocktailsList = cocktails
                    adapter.notifyDataSetChanged()

                    if (recyclerView.visibility == View.GONE)
                        recyclerView.visibility = View.VISIBLE
                }
            }

            when(resource.status) {
                Status.ERROR ->
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()

                Status.SUCCESS ->
                    setData(resource.data)

                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchViewWithSuggestions = SearchViewWithSuggestions(
            menu.findItem(R.id.search_menu)?.actionView as SearchView,
            context,
            searchViewModel.loadSuggestions(),
            viewLifecycleOwner,
            {query -> searchViewModel.selectQuery(query)},
            {selection -> searchViewModel.selectSuggestion(selection)},
            {hideKeyboard()}
        )
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
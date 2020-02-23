package com.education.cocktails.ui.search

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.GRID_LAYOUT_SPAN_COUNT
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail
import com.education.cocktails.model.Suggestion
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
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CocktailsAdapter

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
        searchView = menu.findItem(R.id.search_menu)?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_query_hint)

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.suggest_text_1)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.suggestion_row, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!(query.isNullOrEmpty() && query.isNullOrBlank()))
                    searchViewModel.selectQuery(query)
                hideKeyboard()

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2))
                query?.let { str ->
                    searchViewModel.loadSuggestions().observe(viewLifecycleOwner) { suggestionsList ->
                        suggestionsList.forEach { suggestion ->
                            if (suggestion.name.contains(str, true))
                                cursor.addRow(arrayOf(suggestion.id, suggestion.name, suggestion.table))
                        }

                        cursorAdapter.changeCursor(cursor)
                    }
                }

                return true
            }
        })

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                hideKeyboard()
                val selection = Suggestion(
                    cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)),
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2))
                )

                searchView.setQuery(selection.name, false)

                // TODO Do something with selection
                searchViewModel.selectSuggestion(selection)

                return true
            }
        })
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
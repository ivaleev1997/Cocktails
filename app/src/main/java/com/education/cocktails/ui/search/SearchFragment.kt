package com.education.cocktails.ui.search

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.viewModels
import com.education.cocktails.AppViewModelFactory
import com.education.cocktails.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchView = menu.findItem(R.id.search_menu)?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_query_hint)

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.search_menu)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.fragment_cocktails_search, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        val suggestions = listOf("Apple", "Blueberry","Blue", "Carrot", "Daikon")

        searchView.suggestionsAdapter = cursorAdapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    suggestions.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {

                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                // Do something with selection
                return true
            }

        })
    }
}
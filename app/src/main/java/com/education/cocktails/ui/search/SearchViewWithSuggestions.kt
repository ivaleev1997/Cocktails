package com.education.cocktails.ui.search

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.education.cocktails.R
import com.education.cocktails.model.Suggestion

class SearchViewWithSuggestions(
    private val searchView: SearchView,
    context: Context?,
    listSuggestionsLiveData: LiveData<List<Suggestion>>,
    lifecycleOwner: LifecycleOwner,
    queryTextSubmit: (String) -> Unit,
    selectSuggestion: (Suggestion) -> Unit,
    hideKeyboard: () -> Unit
) {
    init {
        searchView.queryHint = context?.getString(R.string.search_query_hint)
        createAdapter(context)
        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(setOnQueryTextListener(
            listSuggestionsLiveData,
            lifecycleOwner,
            queryTextSubmit,
            hideKeyboard
        ))

        searchView.setOnSuggestionListener(setOnSuggestionsListener(
            hideKeyboard,
            selectSuggestion
        ))
    }

    private lateinit var cursorAdapter: SimpleCursorAdapter

    private fun createAdapter(context: Context?) {
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.suggest_text_1)
        cursorAdapter = SimpleCursorAdapter(context, R.layout.suggestion_row, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
    }

    private fun setOnQueryTextListener(
        listSuggestionsLiveData: LiveData<List<Suggestion>>,
        lifecycleOwner: LifecycleOwner,
        queryTextSubmit: (String) -> Unit,
        hideKeyboard: () -> Unit
    ): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!(query.isNullOrEmpty() && query.isNullOrBlank())) {
                    queryTextSubmit(query)
                    searchView.setQuery(query, false)
                }

                hideKeyboard()

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2))
                query?.let { str ->
                    listSuggestionsLiveData.observe(lifecycleOwner) { suggestionsList ->
                        suggestionsList.forEach { suggestion ->
                            if (suggestion.name.contains(str, true))
                                cursor.addRow(arrayOf(suggestion.id, suggestion.name, suggestion.table))
                        }

                        cursorAdapter.changeCursor(cursor)
                    }
                }

                return true
            }
        }
    }

    private fun setOnSuggestionsListener(
        hideKeyboard: () -> Unit,
        selectSuggestion: (Suggestion) -> Unit
    ): SearchView.OnSuggestionListener {
        return object : SearchView.OnSuggestionListener {
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

                selectSuggestion(selection)

                return true
            }
        }
    }
}
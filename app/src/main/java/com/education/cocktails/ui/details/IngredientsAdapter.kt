package com.education.cocktails.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.education.cocktails.R

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    var ingredientsWithMeasure = listOf<Pair<String, String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_item, parent, false)

        return IngredientsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsWithMeasure.size
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(ingredientsWithMeasure[position])
    }

    inner class IngredientsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val ingredientTextView: TextView = itemView.findViewById(R.id.ingredient_textview)
        private val measureTextView: TextView = itemView.findViewById(R.id.measure_textview)

        fun bind(pair: Pair<String, String>) {
            ingredientTextView.text = pair.first
            measureTextView.text = pair.second
        }
    }
}
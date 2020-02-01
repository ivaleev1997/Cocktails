package com.education.cocktails.ui.mainlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.education.cocktails.R
import com.education.cocktails.model.Cocktail

class CocktailsAdapter(private val detailsCallback: DetailsCallback?):
    RecyclerView.Adapter<CocktailsAdapter.CocktailsRecyclerViewHolder>() {

    var cocktailsList = listOf<Cocktail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cocktails_main_item, parent, false)

        return CocktailsRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int =
        cocktailsList.size

    override fun onBindViewHolder(holder: CocktailsRecyclerViewHolder, position: Int) {
        holder.bind(cocktailsList[position])
        holder.cardView.setOnClickListener {
            //Log.d("COCKTAIL_TAG", "clicked on ${cocktailsList[position].drink}")
            detailsCallback?.onSelected(cocktailsList[position].idDrink)
        }
    }

    inner class CocktailsRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val cardView: CardView = itemView.findViewById(R.id.cocktails_cardview)
        private val imageView: ImageView = itemView.findViewById(R.id.cocktail_image)
        private val textView: TextView = itemView.findViewById(R.id.cocktail_name)

        fun bind(currentCocktail: Cocktail) {
            textView.text = currentCocktail.drink
            Glide.with(imageView)
                .load(currentCocktail.image+ "/preview")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        }
    }

    interface DetailsCallback {
        fun onSelected(id: Long)
    }
}
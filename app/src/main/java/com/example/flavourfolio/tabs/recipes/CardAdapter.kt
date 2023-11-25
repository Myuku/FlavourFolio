package com.example.flavourfolio.tabs.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R

// TODO: Not functional yet. This handles live data and interaction for cards.
class CardAdapter(private val cardList: List<CardModel>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        // Bind data to the card view
        // holder.bind(card)
    }

    override fun getItemCount(): Int = cardList.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views from card_item.xml and bind data
        // Example: val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        // ...

        init {
            // Set long click listener on the itemView
            itemView.setOnLongClickListener {
                // Handle long click, open a dialog or bottom sheet to replace the card
                // Show the list of cards for replacement
                true
            }
        }

        // You can create a bind function to update the views with card data
        // fun bind(card: CardModel) {
        //    titleTextView.text = card.title
        //    ...
        // }
    }
}
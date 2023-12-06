package com.example.flavourfolio.tabs.recipes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Recipe


class CardAdapter :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var recipeList = mutableListOf<Recipe>()
    var onItemClick : ((Recipe) -> Unit) ?= null
    override fun getItemCount(): Int = recipeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun replace(newList: List<Recipe>){
        recipeList.clear()
        recipeList.addAll(newList)
        notifyDataSetChanged()
    }

    // Make changes here if we are adding new elements to the view
    // or if we want to set up listeners
    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleString: TextView = itemView.findViewById(R.id.recipe_card_title)
        private val typeString: TextView = itemView.findViewById(R.id.recipe_card_type_text)
        private val thumbnail: ImageView = itemView.findViewById(R.id.recipe_card_image)

        // create a bind function to update the views with card data
         fun bind(recipe: Recipe) {
            titleString.text = recipe.name
            typeString.text = recipe.type.toString()
            if (recipe.image != null) {
                thumbnail.setImageBitmap(recipe.image)
            }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
        return CardViewHolder(view)
    }

    // Note: This is the function that assigns value to view elements.
    // You will need to first find the elements by doing findViewById in CardViewHolder,
    // then they will be available as an attribute from "holder"
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        // set text in recipe cards
//        holder.titleString.text = recipeList[position].name
//        holder.typeString.text = "Type: " + recipeList[position].type
//
//        // set image
//        val bitmap = recipeList[position].image?.let {
//            BitmapFactory.decodeByteArray(recipeList[position].image, 0, it.size)
//        }
//        holder.thumbnail.setImageBitmap(bitmap)
        val recipe = recipeList[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }
    }
}
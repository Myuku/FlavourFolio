package com.example.flavourfolio.tabs.recipes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Recipe


class CardAdapter(private val context: Context, private val viewModel: RecipesViewModel) :
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
        private val tvStepAmount: TextView = itemView.findViewById(R.id.tvStepAmount)
        private val tvTimeRequired: TextView = itemView.findViewById(R.id.tvTimeRequired)

        // create a bind function to update the views with card data
         fun bind(recipe: Recipe) {
            titleString.text = recipe.name
            typeString.text = recipe.type.toString()
            viewModel.recipeLength(recipe.rid).observe(context as LifecycleOwner) { length ->
                tvStepAmount.text = context.resources.getString(R.string.recipe_card_step_count, length)
            }
            viewModel.totalTime(recipe.rid).observe(context as LifecycleOwner) { time ->
                val hours = time / 3600
                val minutes = (time % 3600) / 60
                val seconds = time % 60
                var timeString = context.resources.getString(R.string.recipe_card_time_required)
                if (hours != 0L) {
                    timeString += String.format(" %d Hours", hours)
                }
                if (minutes != 0L) {
                    timeString += String.format(" %d Minutes", minutes)
                }
                if (seconds != 0L) {
                    timeString += String.format(" %d Seconds", seconds)
                }
                tvTimeRequired.text = timeString
            }
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
        val recipe = recipeList[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }
    }
}
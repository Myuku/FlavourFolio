package com.example.flavourfolio.tabs.addRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Action


//class ActionAdapter :
//    RecyclerView.Adapter<ActionAdapter.ViewHolder>() {
//    var items: ArrayList<Action> = ArrayList<Action>()
//    fun addItem(item: Action) {
//        items.add(item)
//        notifyDataSetChanged()
//    }
//
//    val item: ArrayList<Action>
//        get() = items
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val itemView: View = inflater.inflate(R.layout.action_item, parent, false)
//        return ViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item: Action = items[position]
//        holder.setItem(item)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var set: TextView
//
//        init {
//            set = itemView.findViewById<TextView>(R.id.set)
//        }
//
//        fun setItem(item: Action) {
//            set.text = "TEST"
//        }
//    }
//}
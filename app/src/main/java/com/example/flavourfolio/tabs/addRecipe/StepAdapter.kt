package com.example.flavourfolio.tabs.addRecipe

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.database.Action
import com.example.flavourfolio.database.Step

//
//class StepAdapter : RecyclerView.Adapter<StepAdapter.ViewHolder>() {
//    var context: Context? = null
//    var stepItems: ArrayList<Step> = ArrayList<Step>()
//    var listener: OnStepItemClickListener? = null
//    fun setOnStepClickListener(listener: OnStepItemClickListener?) {
//        this.listener = listener
//    }
//
//    fun addItem(item: Step) {
//        stepItems.add(item)
//    }
//
//    fun addActionItem(actionAdapter: ActionAdapter) {
//       actionAdapter.addItem(Action())
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val itemView: View = inflater.inflate(R.layout.step_item, parent, false)
//        val holder: ViewHolder = ViewHolder(itemView)
//        val actionAdapter = ActionAdapter()
//
//        holder.setActionRecyClerView()
//        holder.action!!.adapter = actionAdapter
//        actionAdapter.addItem(Action())
//        return holder
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val curStepItem: Step = stepItems[position]
//        holder.setItems(curStepItem)
//    }
//
//    override fun getItemCount(): Int {
//        return stepItems.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var step: TextView? = null
//        var addSet: Button? = null
//        var action: RecyclerView? = null
//
//        init {
//            initViews()
//            addSet!!.setOnClickListener {
//                val stepActionAdapter: ActionAdapter? =
//                    action!!.adapter as ActionAdapter?
//                listener!!.OnItemClick(stepActionAdapter)
//            }
//        }
//
//        private fun initViews() {
//            step = itemView.findViewById<TextView>(R.id.step)
//            action = itemView.findViewById<RecyclerView>(R.id.action_step)
//            addSet = itemView.findViewById<Button>(R.id.add_set)
//        }
//
//        private fun setItems(stepItem: Step) {
//            step.setText(stepItem.getStep())
//        }
//
//        fun setActionRecyClerView() {
//            action!!.layoutManager =
//                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//            action!!.setHasFixedSize(true)
//        }
//    }
//
//    interface OnStepItemClickListener {
//        fun OnItemClick(adapter: ActionAdapter?)
//    }
//}
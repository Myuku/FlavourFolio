package com.example.flavourfolio.tabs.addRecipe

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Action
import com.example.flavourfolio.database.Step
import com.example.flavourfolio.enums.AffixType

class StepAdapter(private val context: Context, private val recipeId: Int,
                  private val viewModel: AddRecipeViewModel) :
    RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    private var holders: ArrayList<ViewHolder> = ArrayList()
    val stepItems: ArrayList<Step> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.add_recipe_step, parent, false)
        val viewHolder = ViewHolder(itemView)
        holders.add(viewHolder)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return stepItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curStep: Step = stepItems[position]
        holder.bind(curStep)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepNum: TextView = itemView.findViewById(R.id.tvStepNum)
        val btnDeleteStep: Button = itemView.findViewById(R.id.btnDeleteStep)

        val spnrAction: Spinner = itemView.findViewById(R.id.add_recipe_action_spinner)
        val etFood: EditText = itemView.findViewById(R.id.add_recipe_food_edittext)

        val cbIn: CheckBox = itemView.findViewById(R.id.cbIn)
        val spnrTool: Spinner = itemView.findViewById(R.id.add_recipe_tool_spinner)

        val cbFor: CheckBox = itemView.findViewById(R.id.cbFor)
        val npHours: NumberPicker = itemView.findViewById(R.id.npHours)
        val npMinutes: NumberPicker = itemView.findViewById(R.id.npMinutes)
        val npSeconds: NumberPicker = itemView.findViewById(R.id.npSeconds)

        val cbUntil: CheckBox = itemView.findViewById(R.id.cbUntil)
        val spnrState: Spinner = itemView.findViewById(R.id.add_recipe_state_spinner)

        init {
            spnrAction.adapter = ArrayAdapter.createFromResource(
                context,
                R.array.action_values,
                android.R.layout.simple_spinner_dropdown_item
            )
            spnrTool.adapter = ArrayAdapter.createFromResource(
                context,
                R.array.tool_values,
                android.R.layout.simple_spinner_dropdown_item
            )
            spnrState.adapter = ArrayAdapter.createFromResource(
                context,
                R.array.state_values,
                android.R.layout.simple_spinner_dropdown_item
            )

            npHours.minValue = 0
            npHours.maxValue = 24
            npMinutes.minValue = 0
            npMinutes.maxValue = 59
            npSeconds.minValue = 0
            npSeconds.maxValue = 59
        }
        @SuppressLint("NotifyDataSetChanged")
        fun bind(step: Step) {
            val stepName = "Step ${step.step}"
            tvStepNum.text = stepName
            btnDeleteStep.setOnClickListener {
                stepItems.remove(step)
                for (i in bindingAdapterPosition..<itemCount) {
                    stepItems[i].step -= 1
                }
                notifyDataSetChanged()
            }
        }

    }
    fun addItem() {
        stepItems.add(Step(rid = recipeId, step = itemCount+1, action = "", food = ""))
        notifyItemInserted(itemCount-1)
    }

    fun finalize() {
        for (i in 0..<itemCount) {
            stepItems[i].action = holders[i].spnrAction.selectedItem.toString()
            stepItems[i].food = holders[i].etFood.text.toString()

            viewModel.insertStep(stepItems[i]).observe(context as LifecycleOwner) { stepId ->
                if (holders[i].cbIn.isChecked) {
                    viewModel.insertAction(Action(
                        sid = stepId.toInt(),
                        affix = AffixType.IN,
                        detail = holders[i].spnrTool.selectedItem.toString()
                    ))
                }
                if (holders[i].cbFor.isChecked) {
                    val timeArray = intArrayOf(
                        holders[i].npHours.value,
                        holders[i].npMinutes.value,
                        holders[i].npSeconds.value
                    )
                    val time = timeArray.joinToString("-") { s: Int ->
                        if (s < 10) { "0$s" } else { "$s" }
                    }
                    viewModel.insertAction(Action(
                        sid = stepId.toInt(),
                        affix = AffixType.FOR,
                        detail = time
                    ))
                }
                if (holders[i].cbUntil.isChecked) {
                    viewModel.insertAction(Action(
                        sid = stepId.toInt(),
                        affix = AffixType.UNTIL,
                        detail = holders[i].spnrState.selectedItem.toString()
                    ))
                }
            }
        }
    }
}
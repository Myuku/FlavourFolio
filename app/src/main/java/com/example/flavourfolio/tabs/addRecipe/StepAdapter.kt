package com.example.flavourfolio.tabs.addRecipe

import android.content.Context
import android.content.DialogInterface
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.FlavourFolioApplication
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Step
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class StepAdapter(private val context: Context, val recipeId: Int, var stepItems: List<Step>) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    private val stepRepository = (context.applicationContext as FlavourFolioApplication).stepRepository
    private val actionRepository = (context.applicationContext as FlavourFolioApplication).actionRepository

    private var holders: ArrayList<ViewHolder> = ArrayList()

    fun replace(newList: List<Step>){
        stepItems = newList
    }


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
        fun bind(step: Step) {

            tvStepNum.text = "Step ${step.step}"
            btnDeleteStep.setOnClickListener {
                CoroutineScope(IO).launch {
                    stepRepository.deleteStep(step.sid)
                }
                notifyItemRangeChanged(0, itemCount)
            }

            // Tried to be able to change previous steps but lookin kinda broken
//            spnrAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    val newAction = holders[step.step-1].spnrAction.selectedItem.toString()
//                    step.action = newAction
//                }
//            }
//            etFood.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//                override fun onTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//                override fun afterTextChanged(editable: Editable) {
//                    val newFood = holders[step.step-1].etFood.text.toString()
//                    step.food = newFood
//                }
//            })

        }

    }
    fun addItem() {
        if (itemCount > 0) {
            // update previous step
            val step = stepItems[itemCount-1]
            step.action = holders[itemCount-1].spnrAction.selectedItem.toString()
            step.food = holders[itemCount-1].etFood.text.toString()
            CoroutineScope(IO).launch {
                stepRepository.update(step)
            }
        }
        // add new item
        CoroutineScope(IO).launch {
            stepRepository.insert(Step(action = "", food = "", step = itemCount+1, rid = recipeId))
        }
    }





}
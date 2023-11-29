package com.example.flavourfolio.tabs.addRecipe


import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.widget.addTextChangedListener
import com.example.flavourfolio.R
import com.example.flavourfolio.enums.RecipeType


class AddRecipeDialog : AppCompatDialogFragment() {

    private lateinit var etRecipeName: EditText
    private lateinit var spnrRecipeType: Spinner
    private lateinit var recipeName: String
    private var recipeType: Int = -1

    companion object {
        const val NAME_KEY = "NewRecipeName"
        const val TYPE_KEY = "NewRecipeType"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_recipe_prompt, null)

        etRecipeName = view.findViewById(R.id.etRecipeName)
        spnrRecipeType = view.findViewById(R.id.spnrRecipeType)
        spnrRecipeType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            RecipeType.values()
        )

        val dialog = builder
            .setView(view)
            .setTitle("Add New Recipe")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Create") { _, _ ->
                recipeName = etRecipeName.text.toString()
                recipeType = spnrRecipeType.selectedItemPosition

                val intent = Intent(requireContext(), AddRecipeActivity::class.java)
                intent.putExtra(NAME_KEY, recipeName)
                intent.putExtra(TYPE_KEY, recipeType)
                startActivity(intent)
            }
            .show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false

        etRecipeName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = etRecipeName.text.toString().isNotEmpty()
            }
        })
        return dialog
    }






}

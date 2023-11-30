package com.example.flavourfolio.tabs.addRecipe

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.FlavourFolioApplication
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.enums.RecipeType
import com.example.flavourfolio.tabs.addRecipe.AddRecipeViewModel.AddRecipeViewModelFactory
import kotlinx.coroutines.launch


class AddRecipeActivity : AppCompatActivity() {

    private lateinit var rvStep: RecyclerView
    private lateinit var btnAddStep: Button
    private lateinit var stepAdapter: StepAdapter
    private lateinit var stepLayoutManager: LinearLayoutManager

    private var recipeId = -1
    private lateinit var recipeName: String
    private lateinit var recipeType: RecipeType
    private lateinit var recipe: Recipe

    private val viewModel: AddRecipeViewModel by viewModels {
        AddRecipeViewModelFactory(
            (application as FlavourFolioApplication).recipeRepository,
            (application as FlavourFolioApplication).stepRepository,
            (application as FlavourFolioApplication).actionRepository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_recipe_activity)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
        }
        recipeName = intent.getStringExtra(AddRecipeDialog.NAME_KEY).toString()
        recipeType = RecipeType.values()[intent.getIntExtra(AddRecipeDialog.TYPE_KEY, 0)]
        val newRecipe = Recipe(name = recipeName, type = recipeType)

        initViews()
        setStepRecyclerview()

        viewModel.insertRecipe(newRecipe).observe(this) { userId ->
            recipeId = userId.toInt()
            stepAdapter = StepAdapter(this, recipeId, emptyList())
            rvStep.adapter = stepAdapter
            // can only access rid from observe
        }
        viewModel.currentSteps.observe(this) { steps ->
            stepAdapter.replace(steps)
            stepAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        rvStep = findViewById(R.id.rvStep)
        btnAddStep = findViewById(R.id.btnAddStep)
        btnAddStep.setOnClickListener {
            addStep()
        }
    }

    private fun setStepRecyclerview() {
        stepLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rvStep.layoutManager = stepLayoutManager
    }

    private fun addStep() {
        stepAdapter.addItem()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        title = "Add New Recipe"
        menuInflater.inflate(R.menu.menu_without_dropdowns, menu)
        return true
    }
}
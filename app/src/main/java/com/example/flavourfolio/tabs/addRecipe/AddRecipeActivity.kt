package com.example.flavourfolio.tabs.addRecipe

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.FlavourFolioApplication
import com.example.flavourfolio.R
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.enums.RecipeType
import com.example.flavourfolio.tabs.addRecipe.AddRecipeViewModel.AddRecipeViewModelFactory


class AddRecipeActivity : AppCompatActivity() {

    private lateinit var rvStep: RecyclerView
    private lateinit var btnAddStep: Button
    //private lateinit var stepAdapter: StepAdapter
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









        viewModel.insertRecipe(newRecipe).observe(this) { userId ->
            recipeId = userId.toInt()
            // can only access rid from observe

            val actionBar = supportActionBar
            actionBar?.title = recipeId.toString()
        }



//        initViews()
//        setStepRecyclerview()
//
//        stepAdapter = StepAdapter()
//        rvStep.adapter = stepAdapter
//
//        // add detail item
//        stepAdapter.setOnStepClickListener(object : StepAdapter.OnStepItemClickListener {
//            override fun OnItemClick(adapter: ActionAdapter?) {
//                stepAdapter.addActionItem(ActionAdapter())
//            }
//        })
    }

    private fun initViews() {
        rvStep = findViewById(R.id.rvStep)
        btnAddStep = findViewById(R.id.btnAddStep)
    }

    private fun setStepRecyclerview() {
        stepLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rvStep.layoutManager = stepLayoutManager
    }

//    fun addRoutine(action: String, food: String) {
//        stepAdapter.addItem(Step(action = action, food = food, step = stepAdapter.itemCount, rid = recipeId))
//        stepLayoutManager.scrollToPositionWithOffset(0, 0)
//    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        title = "Add New Recipe"
        menuInflater.inflate(R.menu.menu_without_dropdowns, menu)
        return true
    }
}
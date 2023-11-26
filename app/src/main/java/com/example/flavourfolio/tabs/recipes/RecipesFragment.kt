package com.example.flavourfolio.tabs.recipes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flavourfolio.R
import com.example.flavourfolio.database.AppDatabase
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeDao
import com.example.flavourfolio.database.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private lateinit var database: AppDatabase
    private lateinit var recipeDAO: RecipeDao
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var viewModel: RecipesViewModel
    private lateinit var viewModelFactory: RecipesViewModelFactory

    companion object {
        fun newInstance() = RecipesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        val dessertsView: RecyclerView = view.findViewById(R.id.recipes_desserts_recyclerview)
        val dinnerView: RecyclerView = view.findViewById(R.id.recipes_dinner_recyclerview)
        val breakfastView: RecyclerView = view.findViewById(R.id.recipes_breakfast_recyclerview)
        val lunchView: RecyclerView = view.findViewById(R.id.recipes_lunch_recyclerview)

        database = AppDatabase.getInstance(requireContext())
        recipeDAO = database.recipeDao
        recipeRepository = RecipeRepository(recipeDAO)
        viewModelFactory = RecipesViewModelFactory(recipeRepository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[RecipesViewModel::class.java]

        dessertsView.layoutManager = LinearLayoutManager(requireContext())
        dinnerView.layoutManager = LinearLayoutManager(requireContext())
        breakfastView.layoutManager = LinearLayoutManager(requireContext())
        lunchView.layoutManager = LinearLayoutManager(requireContext())

        // Set adapter to the custom card adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dessertsFlow.collect { desserts ->
                dessertsView.adapter = CardAdapter(desserts)
            }
            viewModel.dinnerFlow.collect { dinner ->
                dinnerView.adapter = CardAdapter(dinner)
            }
            viewModel.breakfastFlow.collect { breakfast ->
                breakfastView.adapter = CardAdapter(breakfast)
            }
            viewModel.lunchFlow.collect { lunch ->
                lunchView.adapter = CardAdapter(lunch)
            }
        }

        // TEST: Inserting a recipe using coroutines
        val recipe = Recipe(name = "Pancakes", image = null, type = "Breakfast")
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            recipeRepository.insert(recipe)
        }

        return view
    }

}
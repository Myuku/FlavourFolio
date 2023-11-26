package com.example.flavourfolio.tabs.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeRepository
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {
    val allRecipesFlow: Flow<List<Recipe>> = repository.allRecipes
    val dessertsFlow: Flow<List<Recipe>> = repository.dessertRecipes
    val dinnerFlow: Flow<List<Recipe>> = repository.dinnerRecipes
    val breakfastFlow: Flow<List<Recipe>> = repository.breakfastRecipes
    val lunchFlow: Flow<List<Recipe>> = repository.lunchRecipes

    suspend fun insert(recipe: Recipe) {
        repository.insert(recipe)
    }

    suspend fun getRecipe(id: Int): Recipe {
        return repository.getRecipe(id)
    }

}

class RecipesViewModelFactory (private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(RecipesViewModel::class.java))
            return RecipesViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
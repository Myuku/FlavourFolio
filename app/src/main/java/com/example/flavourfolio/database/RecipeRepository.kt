package com.example.flavourfolio.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()
    val dessertRecipes: Flow<List<Recipe>> = recipeDao.getDessertRecipes()
    val dinnerRecipes: Flow<List<Recipe>> = recipeDao.getDinnerRecipes()
    val breakfastRecipes: Flow<List<Recipe>> = recipeDao.getBreakfastRecipes()
    val lunchRecipes: Flow<List<Recipe>> = recipeDao.getLunchRecipes()


    suspend fun getRecipe(id: Int): Recipe {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipe(id)
        }
    }

    suspend fun insert(recipe: Recipe) {
        CoroutineScope(Dispatchers.IO).launch{
            recipeDao.insert(recipe)
        }
    }
}
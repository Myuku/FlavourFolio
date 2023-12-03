package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val dessertRecipes: Flow<List<Recipe>> = recipeDao.getDessertRecipes()
    val dinnerRecipes: Flow<List<Recipe>> = recipeDao.getDinnerRecipes()
    val breakfastRecipes: Flow<List<Recipe>> = recipeDao.getBreakfastRecipes()
    val lunchRecipes: Flow<List<Recipe>> = recipeDao.getLunchRecipes()

    @WorkerThread
    suspend fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }

    @WorkerThread
    suspend fun delete(rid: Int) {
        recipeDao.delete(rid)
    }

}
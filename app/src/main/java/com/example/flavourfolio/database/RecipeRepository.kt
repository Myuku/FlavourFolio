package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import com.example.flavourfolio.enums.RecipeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class RecipeRepository(private val recipeDao: RecipeDao) {

    lateinit var dessertRecipes: Flow<List<Recipe>>
    lateinit var dinnerRecipes: Flow<List<Recipe>>
    lateinit var breakfastRecipes: Flow<List<Recipe>>
    lateinit var lunchRecipes: Flow<List<Recipe>>
    init {
        sortByDate()
    }
    @WorkerThread
    fun sortAlphabetical() {
        val allRecipes = recipeDao.getAllRecipesByAlpha().distinctUntilChanged()
        dessertRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DESSERT }}
        dinnerRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DINNER }}
        breakfastRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.BREAKFAST }}
        lunchRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.LUNCH }}
    }

    @WorkerThread
    fun sortByDate() {
        val allRecipes = recipeDao.getAllRecipesByDate().distinctUntilChanged()
        dessertRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DESSERT }}
        dinnerRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DINNER }}
        breakfastRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.BREAKFAST }}
        lunchRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.LUNCH }}
    }

    @WorkerThread
    fun searchBy(str: String) {
        val allRecipes = recipeDao.getAllRecipesBySearch(str).distinctUntilChanged()
        dessertRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DESSERT }}
        dinnerRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.DINNER }}
        breakfastRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.BREAKFAST }}
        lunchRecipes = allRecipes.map { it.filter { r -> r.type == RecipeType.LUNCH }}
    }


    @WorkerThread
    suspend fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }

    @WorkerThread
    suspend fun getRecipe(rid: Int): Recipe {
        return recipeDao.getRecipe(rid)
    }

    @WorkerThread
    suspend fun delete(rid: Int) {
        recipeDao.delete(rid)
    }

}
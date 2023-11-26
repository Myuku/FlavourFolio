package com.example.flavourfolio.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE rid IS :id")
    suspend fun getRecipe(id: Int): Recipe

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE type = 'dessert'")
    fun getDessertRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE type = 'dinner'")
    fun getDinnerRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE type = 'breakfast'")
    fun getBreakfastRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE type = 'lunch'")
    fun getLunchRecipes(): Flow<List<Recipe>>
}

@Dao
interface StepDao {
    @Insert
    suspend fun insert(step: Step)

    @Query("SELECT * FROM steps WHERE rid = :recipeId")
    suspend fun getStepsForRecipe(recipeId: Int): List<Step>
    // Add other required queries
}

@Dao
interface DetailDao { // can't use action since it's a protected word
    @Insert
    suspend fun insert(detail: Detail)

    @Query("SELECT * FROM details WHERE sid = :stepId")
    suspend fun getDetailsForStep(stepId: Int): List<Detail>
    // Add other required queries
}
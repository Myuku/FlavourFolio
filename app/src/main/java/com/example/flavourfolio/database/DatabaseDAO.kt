package com.example.flavourfolio.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe)

    @Query("DELETE FROM recipes_table WHERE recipe_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM recipes_table WHERE recipe_id IS :id")
    suspend fun getRecipe(id: Int): Recipe

    @Query("SELECT * FROM recipes_table")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE type = 'DESSERT'")
    fun getDessertRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE type = 'DINNER'")
    fun getDinnerRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE type = 'BREAKFAST'")
    fun getBreakfastRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE type = 'LUNCH'")
    fun getLunchRecipes(): Flow<List<Recipe>>
}

@Dao
interface StepDao {
    @Insert
    suspend fun insert(step: Step)

    @Query("DELETE FROM steps_table WHERE step_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM recipes_table WHERE recipe_id = :recipeId)")
    suspend fun isRecipeExist(recipeId: Int): Boolean

    @Query("SELECT * FROM steps_table WHERE recipe_id = :recipeId")
    fun getStepsForRecipe(recipeId: Int): Flow<List<Step>>
    // Add other required queries

}

@Dao
interface ActionDao {
    @Insert
    suspend fun insert(action: Action)

    @Query("DELETE FROM actions_table WHERE action_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM steps_table WHERE step_id = :stepId)")
    suspend fun isStepExist(stepId: Int): Boolean

    @Query("SELECT * FROM actions_table WHERE step_id = :stepId")
    fun getDetailsForStep(stepId: Int): Flow<List<Action>>
    // Add other required queries
}
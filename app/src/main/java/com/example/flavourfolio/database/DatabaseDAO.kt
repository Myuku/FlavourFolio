package com.example.flavourfolio.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe): Long

    @Query("DELETE FROM recipes_table WHERE recipe_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM recipes_table WHERE recipe_id IS :id")
    suspend fun getRecipe(id: Int): Recipe

    @Query("SELECT * FROM recipes_table ORDER BY recipe_id")
    fun getAllRecipesByDate(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table ORDER BY name")
    fun getAllRecipesByAlpha(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes_table WHERE name LIKE '%' || :str || '%'")
    fun getAllRecipesBySearch(str: String): Flow<List<Recipe>>

    @Query("SELECT * FROM actions_table WHERE step_id IN (SELECT step_id FROM steps_table WHERE recipe_id = :recipeId) AND affix = 'FOR'")
    suspend fun allTimes(recipeId: Int): List<Action>
}

@Dao
interface StepDao {
    @Insert
    suspend fun insert(step: Step): Long

    @Update
    suspend fun update(step: Step)

    @Query("DELETE FROM steps_table WHERE step_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM actions_table WHERE step_id = :stepId AND affix = 'FOR')")
    suspend fun hasTimer(stepId: Int): Boolean

    @Query("SELECT COUNT(*) FROM steps_table WHERE recipe_id = :recipeId")
    suspend fun length(recipeId: Int): Int

    @Query("SELECT * FROM steps_table WHERE recipe_id = :recipeId ORDER BY step_id")
    suspend fun getStepsForRecipe(recipeId: Int): List<Step>
    // Add other required queries

    @Query("SELECT * FROM steps_table")
    fun getAllSteps(): Flow<List<Step>>

}

@Dao
interface ActionDao {
    @Insert
    suspend fun insert(action: Action)

    @Query("DELETE FROM actions_table WHERE action_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM steps_table WHERE step_id = :stepId)")
    suspend fun isStepExist(stepId: Int): Boolean

    @Query("SELECT * FROM actions_table WHERE step_id = :stepId AND affix = 'IN'")
    suspend fun getInForStep(stepId: Int): Action?
    @Query("SELECT * FROM actions_table WHERE step_id = :stepId AND affix = 'FOR'")
    suspend fun getForForStep(stepId: Int): Action?
    @Query("SELECT * FROM actions_table WHERE step_id = :stepId AND affix = 'UNTIL'")
    suspend fun getUntilForStep(stepId: Int): Action?


}
package com.example.flavourfolio

import android.app.Application
import com.example.flavourfolio.database.ActionRepository
import com.example.flavourfolio.database.AppDatabase
import com.example.flavourfolio.database.RecipeRepository
import com.example.flavourfolio.database.StepRepository

class FlavourFolioApplication : Application() {
    private val database by lazy { AppDatabase.getInstance(this) }
    val recipeRepository by lazy { RecipeRepository(database.recipeDao) }
    val stepRepository by lazy { StepRepository(database.stepDao) }
    val actionRepository by lazy { ActionRepository(database.actionDao) }
}
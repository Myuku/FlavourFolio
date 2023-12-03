package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.flavourfolio.database.Action
import com.example.flavourfolio.database.ActionRepository
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeRepository
import com.example.flavourfolio.database.Step
import com.example.flavourfolio.database.StepRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit


class StepsViewModel(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository,
    private val actionRepository: ActionRepository
    ) : ViewModel() {

    lateinit var currRecipe: Recipe
    var recipeId: Int = -1

    var currSteps: List<Step> = emptyList()
    var actionIn: Action? = null
    var actionFor: Action? = null
    var actionUntil: Action? = null

    var currProgress = 0
    var maxSteps = 0
    var recipeTimer: Long = 0

    fun incrementStep(): Int {
        if (currProgress >= maxSteps) {
            recipeId = -1
            return 1
        }
        currProgress++
        return 0
    }
    fun decrementStep(): Int {
        if (currProgress <= 1) { return 1 }
        currProgress--
        return 0
    }

    fun updateRecipe(rid: Int) = viewModelScope.launch {
        recipeId = rid
        currProgress = 1
        maxSteps = stepRepository.length(rid) + 1

        currRecipe = recipeRepository.getRecipe(rid)
        currSteps = stepRepository.retrieveSteps(rid)
    }

    fun retrieveActions(sid: Int) = viewModelScope.launch {
        actionIn = actionRepository.retrieveIn(sid)
        actionFor = actionRepository.retrieveFor(sid)
        actionUntil = actionRepository.retrieveUntil(sid)
    }


    @Suppress("UNCHECKED_CAST")
    class StepsViewModelFactory(
        private val recipeRepository: RecipeRepository,
        private val stepRepository: StepRepository,
        private val actionRepository: ActionRepository
    ) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{
            if(modelClass.isAssignableFrom(StepsViewModel::class.java))
                return StepsViewModel(recipeRepository, stepRepository, actionRepository) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



}
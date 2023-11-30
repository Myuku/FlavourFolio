package com.example.flavourfolio.tabs.addRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.flavourfolio.database.ActionRepository
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeRepository
import com.example.flavourfolio.database.Step
import com.example.flavourfolio.database.StepRepository
import java.lang.IllegalArgumentException

class AddRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository,
    private val actionRepository: ActionRepository
): ViewModel() {

    val stepsLiveData: LiveData<List<Step>> = stepRepository.allSteps.asLiveData()
    private val _recipeId = MutableLiveData<Long>()

    val currentSteps: LiveData<List<Step>>
        get() = _recipeId.switchMap { rid ->
            stepRepository.retrieveSteps(rid.toInt()).asLiveData()
        }

    fun insertRecipe(recipe: Recipe) = liveData {
        val response = recipeRepository.insert(recipe)
        _recipeId.value = response
        emit(response)
    }


    @Suppress("UNCHECKED_CAST")
    class AddRecipeViewModelFactory(
        private val recipeRepository: RecipeRepository,
        private val stepRepository: StepRepository,
        private val actionRepository: ActionRepository
    ) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{
            if(modelClass.isAssignableFrom(AddRecipeViewModel::class.java))
                return AddRecipeViewModel(recipeRepository, stepRepository, actionRepository) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
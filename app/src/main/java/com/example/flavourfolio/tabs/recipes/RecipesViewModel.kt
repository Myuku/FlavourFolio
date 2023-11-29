package com.example.flavourfolio.tabs.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeRepository
import com.example.flavourfolio.database.Step
import com.example.flavourfolio.database.StepRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RecipesViewModel(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository
) : ViewModel() {

    val dessertsLiveData: LiveData<List<Recipe>> = recipeRepository.dessertRecipes.asLiveData()
    val dinnerLiveData: LiveData<List<Recipe>> = recipeRepository.dinnerRecipes.asLiveData()
    val breakfastLiveData: LiveData<List<Recipe>> = recipeRepository.breakfastRecipes.asLiveData()
    val lunchLiveData: LiveData<List<Recipe>> = recipeRepository.lunchRecipes.asLiveData()

    // For testing
    var currentSteps: LiveData<List<Step>>? = null

    fun insert(recipe: Recipe) = viewModelScope.launch {
        recipeRepository.insert(recipe)
    }

    fun deleteDessertAt(position: Int) = viewModelScope.launch {
        val dessertList = dessertsLiveData.value
        if (!dessertList.isNullOrEmpty()){
            val id = dessertList[position].rid
            recipeRepository.delete(id)
        }
    }
    fun deleteDinnerAt(position: Int) = viewModelScope.launch {
        val dinnerList = dinnerLiveData.value
        if (!dinnerList.isNullOrEmpty()){
            val id = dinnerList[position].rid
            recipeRepository.delete(id)
        }
    }
    fun deleteBreakfastAt(position: Int) = viewModelScope.launch {
        val breakfastList = breakfastLiveData.value
        if (!breakfastList.isNullOrEmpty()){
            val id = breakfastList[position].rid
            recipeRepository.delete(id)
        }
    }
    fun deleteLunchAt(position: Int) = viewModelScope.launch {
        val lunchList = lunchLiveData.value
        if (!lunchList.isNullOrEmpty()){
            val id = lunchList[position].rid
            recipeRepository.delete(id)
        }
    }

    // This is really just for testing, wont be needing it anymore
    fun getDessertStepsFor(position: Int) = viewModelScope.launch {
        val dessertList = dessertsLiveData.value
        if (!dessertList.isNullOrEmpty()){
            val id = dessertList[position].rid
            currentSteps = stepRepository.retrieveSteps(id)?.asLiveData()
        }
    }
    fun getDinnerStepsFor(position: Int) = viewModelScope.launch {
        val dinnerList = dinnerLiveData.value
        if (!dinnerList.isNullOrEmpty()){
            val id = dinnerList[position].rid
            currentSteps = stepRepository.retrieveSteps(id)?.asLiveData()
        }
    }
    fun getBreakfastStepsFor(position: Int) = viewModelScope.launch {
        val breakfastList = breakfastLiveData.value
        if (!breakfastList.isNullOrEmpty()){
            val id = breakfastList[position].rid
            currentSteps = stepRepository.retrieveSteps(id)?.asLiveData()
        }
    }
    fun getLunchStepsFor(position: Int) = viewModelScope.launch {
        val lunchList = lunchLiveData.value
        if (!lunchList.isNullOrEmpty()){
            val id = lunchList[position].rid
            currentSteps = stepRepository.retrieveSteps(id)?.asLiveData()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class RecipesViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository
) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(RecipesViewModel::class.java))
            return RecipesViewModel(recipeRepository, stepRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
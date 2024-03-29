package com.example.flavourfolio.tabs.recipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.flavourfolio.database.Recipe
import com.example.flavourfolio.database.RecipeRepository
import com.example.flavourfolio.database.StepRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RecipesViewModel(
    private val recipeRepository: RecipeRepository,
    private val stepRepository: StepRepository,
) : ViewModel() {

    val dessertsLiveData = MutableLiveData<List<Recipe>>()
    val dinnerLiveData = MutableLiveData<List<Recipe>>()
    val breakfastLiveData = MutableLiveData<List<Recipe>>()
    val lunchLiveData = MutableLiveData<List<Recipe>>()

    init {
        refreshLiveData()
    }

    private fun refreshLiveData() {
        viewModelScope.launch {
            recipeRepository.dessertRecipes.collect {
                dessertsLiveData.postValue(it) }
        }
        viewModelScope.launch {
            recipeRepository.dinnerRecipes.collect {
                dinnerLiveData.postValue(it) }
        }
        viewModelScope.launch {
            recipeRepository.breakfastRecipes.collect {
                breakfastLiveData.postValue(it) }
        }
        viewModelScope.launch {
            recipeRepository.lunchRecipes.collect {
                lunchLiveData.postValue(it) }
        }
    }

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

    fun sortAlphabetical() = viewModelScope.launch {
        recipeRepository.sortAlphabetical()
        refreshLiveData()
    }

    fun sortByDate() = viewModelScope.launch {
        recipeRepository.sortByDate()
        refreshLiveData()
    }

    fun searchBy(str: String) = viewModelScope.launch {
        recipeRepository.searchBy(str)
        refreshLiveData()
    }

    fun recipeLength(rid: Int) = liveData {
        val response = stepRepository.length(rid)
        emit(response)
    }

    fun totalTime(rid: Int) = liveData {
        val response = recipeRepository.allTimes(rid)
        emit(response)
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
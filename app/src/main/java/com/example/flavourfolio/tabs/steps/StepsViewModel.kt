package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.flavourfolio.database.ActionRepository
import com.example.flavourfolio.database.Step
import com.example.flavourfolio.database.StepRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit


class StepsViewModel(
    private val stepRepository: StepRepository,
    private val actionRepository: ActionRepository
    ) : ViewModel() {

    var currentSteps: LiveData<List<Step>>? = null
    var recipeId: Int = -1

    var currProgress = 1
        private set
    var maxSteps = 10 // TODO: Always do numSteps + 1 to display complete screen
        private set
    var recipeTimer: Long = TimeUnit.MINUTES.toMillis(10)
        private set
    var action = "fry"
        private set
    var subject = "tomato"
        private set


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
        maxSteps = stepRepository.length(rid)
        //currentSteps = stepRepository.retrieveSteps(rid)?.asLiveData()
    }


    @Suppress("UNCHECKED_CAST")
    class StepsViewModelFactory(
        private val stepRepository: StepRepository,
        private val actionRepository: ActionRepository
    ) : ViewModelProvider.Factory {
        override fun<T: ViewModel> create(modelClass: Class<T>) : T{
            if(modelClass.isAssignableFrom(StepsViewModel::class.java))
                return StepsViewModel(stepRepository, actionRepository) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



}
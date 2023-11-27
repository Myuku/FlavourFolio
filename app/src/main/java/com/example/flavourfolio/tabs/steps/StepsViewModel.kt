package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit


class StepsViewModel : ViewModel() {
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
        if (currProgress == maxSteps) { return 1 }
        currProgress++
        return 0
    }
    fun decrementStep(): Int {
        if (currProgress == 1) { return 1 }
        currProgress--
        return 0
    }



}
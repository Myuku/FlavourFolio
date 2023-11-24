package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.ViewModel

class StepsViewModel : ViewModel() {
    var currProgress = 1
        private set
    var maxSteps = 10 // TODO: Always do numSteps + 1 to display complete screen
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
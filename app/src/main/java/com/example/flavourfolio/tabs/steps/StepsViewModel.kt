package com.example.flavourfolio.tabs.steps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepsViewModel : ViewModel() {
    var counter = 0
        private set

    fun incrementCounter() {
        counter++
    }
}
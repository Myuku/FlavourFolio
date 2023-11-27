package com.example.flavourfolio.tabs.fridge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flavourfolio.R

class FridgeViewModel : ViewModel() {
    val color = MutableLiveData<Int>()

    init {
        color.value = R.color.very_light_pink
    }

    fun changeColor() {
        if (color.value == R.color.light_pink) {
            color.value = R.color.very_light_pink
        } else {
            color.value = R.color.light_pink
        }
    }
}
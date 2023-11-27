package com.example.flavourfolio.tabs.recipes

data class CardModel(
    val title: String,
    val type: String,
    val ingredientsList: ArrayList<String>,
    val imageByteArray: ByteArray
)
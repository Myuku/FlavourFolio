package com.example.flavourfolio.enums

enum class RecipeType(val value: String) {
    DESSERT("Dessert"),
    DINNER("Dinner"),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch");

    override fun toString(): String {
        return value
    }
}


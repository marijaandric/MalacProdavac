package com.example.front.screens.categories.states

data class ChosenCategoriesState (
    var isLoading : Boolean = true,
    var categoriesBool : Boolean? = false,
    var error : String = ""
)
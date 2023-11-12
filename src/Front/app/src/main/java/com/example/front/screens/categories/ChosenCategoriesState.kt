package com.example.front.screens.categories

data class ChosenCategoriesState (
    var isLoading : Boolean = true,
    var categoriesBool : Boolean? = false,
    var error : String = ""
)
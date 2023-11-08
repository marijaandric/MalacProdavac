package com.example.front.screens.categories

import com.example.front.model.CategoriesDTO

data class ChosenCategoriesState (
    var isLoading : Boolean = true,
    var categoriesBool : Boolean? = false,
    var error : String = ""
)
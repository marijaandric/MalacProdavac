package com.example.front.screens.categories.states

import com.example.front.model.DTO.CategoriesDTO

data class CategoriesState (
    var isLoading : Boolean = true,
    var categories : List<CategoriesDTO>? = emptyList(),
    var error : String = ""
)
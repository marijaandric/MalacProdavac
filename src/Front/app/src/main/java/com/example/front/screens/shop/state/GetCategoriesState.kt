package com.example.front.screens.shop.state

import com.example.front.model.DTO.CategoriesDTO

data class GetCategoriesState (
    var isLoading : Boolean = true,
    var categories : List<CategoriesDTO>? = emptyList(),
    var error : String = ""
)

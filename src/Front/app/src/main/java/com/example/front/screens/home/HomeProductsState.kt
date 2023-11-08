package com.example.front.screens.home

import com.example.front.model.CategoriesDTO
import com.example.front.model.HomeProduct

data class HomeProductsState (
    var isLoading : Boolean = true,
    var products : List<HomeProduct>? = emptyList(),
    var error : String = ""
)
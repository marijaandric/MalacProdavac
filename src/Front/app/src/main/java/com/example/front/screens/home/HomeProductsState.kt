package com.example.front.screens.home

import com.example.front.model.DTO.HomeProductDTO

data class HomeProductsState (
    var isLoading : Boolean = true,
    var products : List<HomeProductDTO>? = emptyList(),
    var error : String = ""
)
package com.example.front.screens.shop.state

import com.example.front.model.DTO.ProductDisplayDTO

data class ProductDisplayState (
    var isLoading : Boolean = true,
    var displayProduct : ProductDisplayDTO? = null,
    var error : String = ""
)
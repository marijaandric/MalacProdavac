package com.example.front.screens.products.state

import com.example.front.model.DTO.OrdersPagesDTO
import com.example.front.model.DTO.ProductDTO

data class ProductsState (
    var isLoading : Boolean = true,
    var products : List<ProductDTO>? = emptyList(),
    var error : String = ""
)
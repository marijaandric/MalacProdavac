package com.example.front.screens.shop.state

import com.example.front.model.DTO.ProductDTO
import com.example.front.model.DTO.ShopDetailsDTO

data class ProductState (
    var isLoading : Boolean = true,
    var products : List<ProductDTO>? = emptyList(),
    var error : String = ""
)
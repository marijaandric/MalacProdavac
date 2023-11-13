package com.example.front.screens.product

import com.example.front.model.DTO.HomeProductDTO
import com.example.front.model.product.ProductInfo

data class ProductProductState (
    var isLoading : Boolean = true,
    var product : ProductInfo? = null,
    var error : String = ""
)
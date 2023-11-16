package com.example.front.screens.product

import com.example.front.model.product.ProductInfo
import com.example.front.model.product.ProductReviewUserInfo


data class ReviewProductState (
    var isLoading : Boolean = true,
    var reviews : List<ProductReviewUserInfo>? = emptyList(),
    var error : String = ""
)
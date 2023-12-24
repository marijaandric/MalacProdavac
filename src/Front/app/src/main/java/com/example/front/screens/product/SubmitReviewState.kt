package com.example.front.screens.product

import com.example.front.model.product.ProductReviewUserInfo
import com.example.front.model.response.SuccessBoolean

data class SubmitReviewState (
    var isLoading : Boolean = true,
    var review : SuccessBoolean? = null,
    var error : String = ""
)
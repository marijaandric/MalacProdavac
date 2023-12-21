package com.example.front.screens.notification.state

import com.example.front.model.response.SuccessBoolean

data class ProductReviewState (
    var isLoading: Boolean = true,
    var productReview: SuccessBoolean? = null,
    var error: String = ""
)
package com.example.front.screens.shop.state

import com.example.front.model.response.Success
import com.example.front.model.response.SuccessBoolean

data class PostReviewState (
    var isLoading : Boolean = true,
    var review : SuccessBoolean? = null,
    var error : String = ""
)
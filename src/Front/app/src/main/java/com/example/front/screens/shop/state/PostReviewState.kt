package com.example.front.screens.shop.state

import com.example.front.model.response.Success

data class PostReviewState (
    var isLoading : Boolean = true,
    var review : Success? = null,
    var error : String = ""
)
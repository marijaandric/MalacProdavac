package com.example.front.screens.shop.state

import com.example.front.model.response.SuccessProductDisplay

data class ProductDisplayState (
    var isLoading : Boolean = true,
    var displayProduct : SuccessProductDisplay? = null,
    var error : String = ""
)
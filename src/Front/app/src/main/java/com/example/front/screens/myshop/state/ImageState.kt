package com.example.front.screens.myshop.state

import com.example.front.model.response.Success
import com.example.front.model.response.SuccessBoolean

data class ImageState(
    var isLoading: Boolean = true,
    var image: SuccessBoolean? = null,
    var error: String = ""
)
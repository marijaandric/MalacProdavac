package com.example.front.screens.shop.state

import com.example.front.model.response.SuccessBoolean

data class NewDisplayState (
    var isLoading : Boolean = true,
    var newProductDisplay : SuccessBoolean? = null,
    var error : String = ""
)
package com.example.front.screens.myshop.state

import com.example.front.model.response.Id
import com.example.front.model.response.SuccessBoolean
import com.example.front.model.response.SuccessInt

data class NewShopState (
    var isLoading : Boolean = true,
    var newShop : SuccessInt? = null,
    var error : String = ""
)
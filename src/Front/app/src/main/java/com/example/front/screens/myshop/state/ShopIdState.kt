package com.example.front.screens.myshop.state

import com.example.front.model.DTO.ShopDTO
import com.example.front.model.response.Id

data class ShopIdState (
    var isLoading : Boolean = true,
    var shopId : Id? = null,
    var error : String = ""
)
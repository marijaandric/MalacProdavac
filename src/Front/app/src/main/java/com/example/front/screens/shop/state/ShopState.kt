package com.example.front.screens.shop.state

import com.example.front.model.DTO.ShopDetailsDTO


data class ShopState (
    var isLoading : Boolean = true,
    var shop : ShopDetailsDTO? = null,
    var error : String = ""
)
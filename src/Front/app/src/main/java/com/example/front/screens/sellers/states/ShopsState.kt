package com.example.front.screens.sellers.states

import com.example.front.model.DTO.ShopDTO

data class ShopsState (
    var isLoading : Boolean = true,
    var shops : List<ShopDTO>? = emptyList(),
    var error : String = ""
)
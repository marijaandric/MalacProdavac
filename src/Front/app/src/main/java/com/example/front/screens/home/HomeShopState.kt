package com.example.front.screens.home

import com.example.front.model.DTO.ShopDTO

data class HomeShopState (
    var isLoading : Boolean = true,
    var shops : List<ShopDTO>? = emptyList(),
    var error : String = ""
)
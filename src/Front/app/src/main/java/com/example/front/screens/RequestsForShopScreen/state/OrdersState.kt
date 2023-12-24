package com.example.front.screens.RequestsForShopScreen.state

import com.example.front.model.DTO.OrdersDTO

data class OrdersState (
    var isLoading : Boolean = true,
    var orders  : OrdersDTO? = null,
    var error : String = ""
)
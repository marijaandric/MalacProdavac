package com.example.front.screens.orders.state

import com.example.front.model.DTO.OrdersPagesDTO

data class OrdersPageState (
    var isLoading : Boolean = true,
    var ordersPage : OrdersPagesDTO? = null,
    var error : String = ""
)
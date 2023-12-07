package com.example.front.screens.orders.state

import com.example.front.model.DTO.OrdersDTO

data class OrdersState (
    var isLoading : Boolean = true,
    var orders : List<OrdersDTO> = emptyList(),
    var error : String = ""
)
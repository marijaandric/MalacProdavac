package com.example.front.screens.order.state

import com.example.front.model.DTO.OrderInfoDTO

data class OrderInfoState (
    var isLoading : Boolean = true,
    var orderInfo : OrderInfoDTO? = null,
    var error : String = ""
)
package com.example.front.screens.delivery.state

import com.example.front.model.DTO.CategoriesDTO
import com.example.front.model.DTO.ReqForDeliveryPersonDTO

data class ReqForDeliveryState (
    var isLoading : Boolean = true,
    var req : List<ReqForDeliveryPersonDTO>? = emptyList(),
    var error : String = ""
)
package com.example.front.screens.shop.state

import com.example.front.model.DTO.MetricsDTO

data class GetMetricsState (
    var isLoading : Boolean = true,
    var metrics : List<MetricsDTO>? = emptyList(),
    var error : String = ""
)
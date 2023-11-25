package com.example.front.screens.shop.state

import com.example.front.model.DTO.ReviewDTO
import com.example.front.model.DTO.ShopDetailsDTO

data class ReviewState (
    var isLoading : Boolean = true,
    var reviews : List<ReviewDTO> = emptyList(),
    var error : String = ""
)
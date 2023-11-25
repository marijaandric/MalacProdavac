package com.example.front.screens.shop.state

import com.example.front.model.DTO.ReviewDTO

data class ShopReviewState (
    var isLoading : Boolean = true,
    var reviews : List<ReviewDTO> = emptyList(),
    var error : String = ""
)
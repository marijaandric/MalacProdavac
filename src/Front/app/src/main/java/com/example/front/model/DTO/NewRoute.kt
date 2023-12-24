package com.example.front.model.DTO

data class NewRoute(
    val userId: Int,
    val startDate: String,
    val startTime: String,
    val startLocation: String,
    val endLocation: String,
    val fixedCost: Int
)
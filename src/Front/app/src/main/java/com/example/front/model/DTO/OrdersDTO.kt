package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class OrdersDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("amount") val amount: Float,
    @SerializedName("status") val status: String,
    @SerializedName("createdOn") val createdOn: String,
)
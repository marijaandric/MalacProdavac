package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class CheckAvailabilityResDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("size") val size: String,
    @SerializedName("quantity") val quantity: Double,
    @SerializedName("quantity") val available: Double,
)
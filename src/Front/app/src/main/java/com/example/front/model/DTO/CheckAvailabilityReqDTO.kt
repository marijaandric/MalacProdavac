package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class CheckAvailabilityReqDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("size") val size: String = "None",
    @SerializedName("quantity") val quantity: Double,
)
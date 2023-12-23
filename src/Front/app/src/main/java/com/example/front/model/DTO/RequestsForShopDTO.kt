package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class RequestsForShopDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("locations") val locations: String,
    @SerializedName("createdOn") val createdOn: String,
    @SerializedName("startAddress") val startAddress: String,
    @SerializedName("endAddress") val endAddress: String,
    @SerializedName("routeDivergence") val routeDivergence: Int
)
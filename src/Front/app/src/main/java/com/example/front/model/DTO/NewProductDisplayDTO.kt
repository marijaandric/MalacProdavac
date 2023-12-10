package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class NewProductDisplayDTO(
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("address") val address: String?
)

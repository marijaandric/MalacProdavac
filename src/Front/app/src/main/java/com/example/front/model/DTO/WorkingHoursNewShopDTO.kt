package com.example.front.model.DTO

import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class WorkingHoursNewShopDTO(
    @SerializedName("day") val day: Int,
    @SerializedName("openingHours") val openingHours: String,
    @SerializedName("closingHours") val closingHours: String
)
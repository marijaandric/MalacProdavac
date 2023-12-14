package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class NewShopDTO (
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("categories") val categories: List<Int>,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursDTO>,
    @SerializedName("pib") val pib: Long,
    @SerializedName("accountNumber") val accountNumber: String
)

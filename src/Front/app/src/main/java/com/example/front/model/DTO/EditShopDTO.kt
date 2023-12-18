package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class EditShopDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("categories") val categories: List<Int>?,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursNewShopDTO>?,
    @SerializedName("pib") val pib: Int?,
    @SerializedName("accountNumber") val accountNumber: String?
)
package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ShopDetailsCheckoutDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("image") val image: String,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursDTO>,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("selfpickup") var selfpickup: Boolean = false,
    @SerializedName("total") var total: Double = 0.0,
    @SerializedName("date") var date: Long? = 0,
)
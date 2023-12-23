package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class DeliveryPersonDTO(
    @SerializedName("deliveryPerson")
    val deliveryPerson: String,
    @SerializedName("deliveryPersonId")
    val deliveryPersonId: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("closestRouteDivergence")
    val closestRouteDivergence: Double
)
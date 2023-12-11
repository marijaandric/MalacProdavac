package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class RouteDetails(
    @SerializedName("locations")
    val locations: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("startTime")
    val startTime: String,

    @SerializedName("stops")
    val stops: List<Stop>
)

data class Stop(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("address")
    val address: String,

    @SerializedName("shopName")
    val shopName: String?
)

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
    val shopName: String?,

    @SerializedName("items")
    val items: List<Item>?
)

data class RouteStopsSection(
    val stops: List<Pair<String, String>>,
    val additionalStops: List<Pair<String, String>>
)

data class Item(
    @SerializedName("name")
    val name: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("metric")
    val metric: String
)

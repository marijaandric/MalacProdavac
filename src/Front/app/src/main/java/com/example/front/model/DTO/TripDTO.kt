package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class Trip(
    @SerializedName("startTime")
    val startTime: String,

    @SerializedName("cost")
    val cost: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("locations")
    val locations: String,

    @SerializedName("createdOn")
    val createdOn: String,

    @SerializedName("startAddress")
    val startAddress: String,

    @SerializedName("endAddress")
    val endAddress: String,

    @SerializedName("routeDivergence")
    val routeDivergence: Int
)
package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class MetricDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

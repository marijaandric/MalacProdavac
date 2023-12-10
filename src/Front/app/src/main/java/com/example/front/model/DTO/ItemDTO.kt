package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ItemDTO(
    @SerializedName("name") val name: String,
    @SerializedName("shop") val shop: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("metric") val metric: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("image") val image: String?
)

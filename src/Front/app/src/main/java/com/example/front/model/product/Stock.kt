package com.example.front.model.product

import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class Stock(
    @SerializedName("Size") val size: String,
    @SerializedName("SizeId") val sizeId: Int,
    @SerializedName("Quantity") val quantity: Int
)

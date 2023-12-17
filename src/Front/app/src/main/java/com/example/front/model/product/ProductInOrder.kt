package com.example.front.model.product

import com.google.gson.annotations.SerializedName

class ProductInOrder (
    @SerializedName("id") val id: Int,
    @SerializedName("sizeId") val sizeId: Int,
    @SerializedName("quantity") val quantity: Int,
)
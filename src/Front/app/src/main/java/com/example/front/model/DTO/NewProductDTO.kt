package com.example.front.model.DTO

import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class NewProductDTO(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("metricId") val metricId: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("salePercentage") val salePercentage: Float,
    @SerializedName("saleMinQuantity") val saleMinQuantity: Float,
    @SerializedName("saleMessage") val saleMessage: String,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("weight") val weight: Float,
)
data class Size(
    @SerializedName("sizeId") val sizeId: Int,
    @SerializedName("quantity") val quantity: Int
)

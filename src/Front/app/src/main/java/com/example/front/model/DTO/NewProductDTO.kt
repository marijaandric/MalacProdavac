package com.example.front.model.DTO

import com.example.front.model.product.Stock
import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class NewProductDTO(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("metricId") val metricId: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("salePercentage") val salePercentage: Float,
    @SerializedName("saleMinQuantity") val saleMinQuantity: Int,
    @SerializedName("saleMessage") val saleMessage: String,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("weight") val weight: Float,
    @SerializedName("sizes") val sizes: List<Size>
)
data class Size(
    @SerializedName("sizeId") val sizeId: Int,
    @SerializedName("quantity") val quantity: Int
)

data class EditProductDTO(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("metricId") val metricId: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("salePercentage") val salePercentage: Float,
    @SerializedName("saleMinQuantity") val saleMinQuantity: Int,
    @SerializedName("saleMessage") val saleMessage: String,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("weight") val weight: Float,
    @SerializedName("sizes") val sizes: List<Stock>
)

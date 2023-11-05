package com.example.front.model

import com.google.gson.annotations.SerializedName

data class HomeProduct (
    @SerializedName("id") val id: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Float,
    @SerializedName("metricId") val metricId: Int,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("subcategoryId") val subcategoryId: Int,
    @SerializedName("salePercentage") val salePercentage: Float,
    @SerializedName("saleMinQuantity") val saleMinQuantity: Int,
    @SerializedName("saleMessage") val saleMessage: String,
    @SerializedName("shop") val shop: ShopDTO,
    @SerializedName("metric") val metric: MetricDTO,
    @SerializedName("category") val category: CategoriesDTO,
    @SerializedName("subcategory") val subcategory: Int,
)

data class MetricDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

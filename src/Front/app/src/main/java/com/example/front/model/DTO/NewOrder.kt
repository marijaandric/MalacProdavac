package com.example.front.model.DTO

import com.example.front.model.product.ProductInOrder
import com.google.gson.annotations.SerializedName

data class NewOrder (
    @SerializedName("userId") val userId: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("paymentMethod") val paymentMethod: Int,
    @SerializedName("deliveryMethod") val deliveryMethod: Int,
    @SerializedName("shippingAddress") val shippingAddress: String,
    @SerializedName("pickupTime") val pickupTime: String,
    @SerializedName("products") val products: List<ProductInOrder>,
)
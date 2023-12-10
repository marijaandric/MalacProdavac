package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class OrderInfoDTO(
    @SerializedName("items") val items: List<ItemDTO>?,
    @SerializedName("shippingAddress") val shippingAddress: String?,
    @SerializedName("paymentMethod") val paymentMethod: String?,
    @SerializedName("deliveryMethod") val deliveryMethod: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("createdOn") val createdOn: String?
)

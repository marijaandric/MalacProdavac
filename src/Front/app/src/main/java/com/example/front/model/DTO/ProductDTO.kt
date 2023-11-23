package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ProductDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Float?,
    @SerializedName("rating") val rating : Int?,
    @SerializedName("image") val image : String,
)
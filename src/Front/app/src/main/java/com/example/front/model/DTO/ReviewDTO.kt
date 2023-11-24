package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ReviewDTO(
    @SerializedName("username") val username: String,
    @SerializedName("image") val image: String?,
    @SerializedName("reviewerId") val reviewerId: Int,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("postedOn") val postedOn: String,
    @SerializedName("reviewer") val reviewer: String?,
    @SerializedName("shop") val shop: String?,
)
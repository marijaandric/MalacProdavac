package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class LeaveReviewDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("comment") val comment: String
)
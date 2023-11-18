package com.example.front.model.DTO

import com.example.front.model.user.UserDTO
import com.google.gson.annotations.SerializedName

data class ShopDTO (
    @SerializedName("id") val id: Int,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("image") val image: String?,
    @SerializedName("owner") val owner: UserDTO?,
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursDTO>,
    @SerializedName("rating") val rating: Float,
)
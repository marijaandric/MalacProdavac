package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ShopDetailsDTO (
    @SerializedName("rating") val rating: Int?,
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("boughtFrom") val boughtFrom: Boolean?,
    @SerializedName("rated") val rated: Boolean,
    @SerializedName("isOwner") val isOwner: Boolean,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("subcategories") val subcategories: List<Any>?,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursDTO>?,
    @SerializedName("pib") val pib: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("ownerId") val ownerId: Int?,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("image") val image: String?,
    @SerializedName("owner") val owner: Any?
)
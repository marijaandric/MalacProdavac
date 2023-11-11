package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ChosenCategoriesDTO (
    @SerializedName("userId") val userId: Int,
    @SerializedName("categoryIds") val categoryIds: List<Int>
)
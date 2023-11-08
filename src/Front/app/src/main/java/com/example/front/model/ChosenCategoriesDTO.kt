package com.example.front.model

import com.google.gson.annotations.SerializedName

data class ChosenCategoriesDTO (
    @SerializedName("userId") val userId: Int,
    @SerializedName("categoryIds") val categoryIds: List<Int>
)
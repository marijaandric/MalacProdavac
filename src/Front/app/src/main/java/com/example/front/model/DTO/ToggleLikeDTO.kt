package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class ToggleLikeDTO (
    @SerializedName("success") val success: Boolean
)
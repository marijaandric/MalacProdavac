package com.example.front.model.response

import com.google.gson.annotations.SerializedName

data class Success (
    @SerializedName("success") val success: String?
)
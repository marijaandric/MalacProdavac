package com.example.front.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id") val userId: Int = -1,
    @SerializedName("error") val error: String? = null
)

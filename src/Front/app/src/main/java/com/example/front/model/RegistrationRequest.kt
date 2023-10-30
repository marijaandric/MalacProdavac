package com.example.front.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("name") val name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("address") val address: String,
    @SerializedName("roleId") val roleId: Int
)

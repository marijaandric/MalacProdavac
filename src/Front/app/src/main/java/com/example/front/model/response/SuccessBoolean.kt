package com.example.front.model.response

import com.google.gson.annotations.SerializedName

data class SuccessBoolean(
    @SerializedName("success") val success: Boolean?
)
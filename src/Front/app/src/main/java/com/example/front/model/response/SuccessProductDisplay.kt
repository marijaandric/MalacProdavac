package com.example.front.model.response

import com.example.front.model.DTO.ProductDisplayDTO
import com.google.gson.annotations.SerializedName

data class SuccessProductDisplay(
    @SerializedName("success") val success: ProductDisplayDTO?
)

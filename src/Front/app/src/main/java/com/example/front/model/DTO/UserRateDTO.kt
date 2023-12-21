package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class UserRateDTO (
    @SerializedName("raterId") val raterId:Int,
    @SerializedName("ratedId") val ratedId:Int,
    @SerializedName("communication") val communication:Int,
    @SerializedName("reliability") val reliability:Int,
    @SerializedName("overallExperience") val overallExperience:Int,
)
package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class RatingDTO (
    @SerializedName("raterId") val raterId:Int,
    @SerializedName("ratedId") val ratedId:Int,
    @SerializedName("communication") val communication:Float,
    @SerializedName("reliability") val reliability:Float,
    @SerializedName("overallExperience") val overallExperience:Float,
    @SerializedName("average") val average:Float,
    @SerializedName("rater") val rater:UserDTO,
    @SerializedName("rated") val rated:UserDTO,
)
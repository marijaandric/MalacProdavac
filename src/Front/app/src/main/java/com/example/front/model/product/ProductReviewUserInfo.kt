package com.example.front.model.product

import com.nimbusds.jose.shaded.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


data class ProductReviewUserInfo(
    @SerializedName("username") val username: String,
    @SerializedName("image") val image: String,
    @SerializedName("reviewerId") val reviewerId: Int,
    @SerializedName("productId") val productId: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("postedOn") val postedOn: String?,
    @SerializedName("reviewer") val reviewer: Any?,
    @SerializedName("product") val product: Any?
) {
    private fun getPostedOnDateTime(): LocalDateTime? {
        return postedOn?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
        }
    }

    // Calculate the difference in days between current date and postedOn date
    fun getDaysSincePosted(): Int? {
        val postedOnDateTime = getPostedOnDateTime()
        return postedOnDateTime?.let {
            val currentDate = LocalDateTime.now()
            val daysDifference = ChronoUnit.DAYS.between(it, currentDate)
            daysDifference.toInt()
        }
    }
}

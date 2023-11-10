package com.example.front.model.product

import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class ProductReviewUserInfo(
    @SerializedName("username") val username: String,
    @SerializedName("image") val image: String,
    @SerializedName("reviewerId") val reviewerId: Int,
    @SerializedName("productId") val productId: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("postedOn") val postedOn: String?, // Assuming the date is a string, you can change the type accordingly
    @SerializedName("reviewer") val reviewer: Any?,     // You can replace 'Any?' with an appropriate data class if needed
    @SerializedName("product") val product: Any?       // You can replace 'Any?' with an appropriate data class if needed
)

package com.example.front.model.product

import com.example.front.model.DTO.ImageDataDTO
import com.example.front.model.DTO.WorkingHoursDTO
import com.google.gson.annotations.SerializedName

data class ProductInfo(
    @SerializedName("shopName") val shopName: String?,
    @SerializedName("metric") val metric: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("subcategory") val subcategory: String?,
    @SerializedName("liked") val liked: Boolean?,
    @SerializedName("bought") val bought: Boolean?,
    @SerializedName("rated") val rated: Boolean?,
    @SerializedName("isOwner") val isOwner: Boolean?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("workingHours") val workingHours: List<WorkingHoursDTO>?,
    @SerializedName("questionsAndAnswers") val questionsAndAnswers: List<QuestionWithAnswer>?,
    @SerializedName("sizes") val sizes: List<Stock>?,
    @SerializedName("images") val images: List<ImageDataDTO>?,
    @SerializedName("id") val productId: Int?,
    @SerializedName("shopId") val shopId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Float?,
    @SerializedName("metricId") val metricId: Int?,
    @SerializedName("categoryId") val categoryId: Int?,
    @SerializedName("subcategoryId") val subcategoryId: Int?,
    @SerializedName("salePercentage") val salePercentage: Float?,
    @SerializedName("saleMinQuantity") val saleMinQuantity: Float?,
    @SerializedName("saleMessage") val saleMessage: String?,
    @SerializedName("shop") val shop: Any?,
    @SerializedName("mass") val mass: Float?
)


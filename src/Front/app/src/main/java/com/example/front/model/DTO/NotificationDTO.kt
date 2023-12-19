package com.example.front.model.DTO

import com.google.gson.annotations.SerializedName

data class NotificationDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("typeId") val typeId: Int,
    @SerializedName("createdOn") val createdOn: String,
    @SerializedName("referenceId") val referenceId: Int,
    @SerializedName("read") val read: Boolean
)
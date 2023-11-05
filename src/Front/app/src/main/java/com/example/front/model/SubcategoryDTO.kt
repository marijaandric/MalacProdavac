package com.example.front.model

import com.google.gson.annotations.SerializedName

data class SubcategoryDTO (
    @SerializedName("id") val id:Int,
    @SerializedName("name") val name:String,
    @SerializedName("categoryId") val categoryId:Int,
    @SerializedName("category") val category:CategoriesDTO,
)
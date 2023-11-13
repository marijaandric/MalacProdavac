package com.example.front.model.user

import com.google.gson.annotations.SerializedName

data class UserEditDTO (
    @SerializedName("id") val id:Int,
    @SerializedName("username") val username:String,
    @SerializedName("address") val address:String,
    @SerializedName("roleId") val roleId:Int,
    @SerializedName("image") val image:String,
    @SerializedName("name") val name:String,
)
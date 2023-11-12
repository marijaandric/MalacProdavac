package com.example.front.model.user

import com.google.gson.annotations.SerializedName

data class UserDTO (
    @SerializedName("id") val id:Int,
    @SerializedName("username") val username:String,
    @SerializedName("name") val name:String,
    @SerializedName("lastname") val lastname:String,
    @SerializedName("email") val email:String
    // nije zavrseno
)
package com.example.front.screens.userprofile

import com.example.front.model.HomeProduct
import com.example.front.model.user.MyProfileDTO

data class MyProfileState (
    var isLoading : Boolean = true,
    var info : MyProfileDTO? = null,
    var error : String = ""
)
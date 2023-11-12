package com.example.front.screens.userprofile

import com.example.front.model.response.LoginResponse

data class EditState (
    var isLoading : Boolean = true,
    var info : LoginResponse? = null,
    var error : String = ""
)
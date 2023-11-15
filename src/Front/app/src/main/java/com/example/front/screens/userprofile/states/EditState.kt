package com.example.front.screens.userprofile.states

import com.example.front.model.response.LoginResponse

data class EditState (
    var isLoading : Boolean = true,
    var info : LoginResponse? = null,
    var error : String = ""
)
package com.example.front.screens.login.state

import com.example.front.model.DTO.CategoriesDTO

data class LoginState (
    var loginState : Boolean? = false,
    var error : String = ""
)
package com.example.front.screens.home.states

import com.example.front.model.DTO.ToggleLikeDTO

data class ToggleLikeState (
    var isLoading : Boolean = true,
    var success : ToggleLikeDTO? = null,
    var error : String = ""
)
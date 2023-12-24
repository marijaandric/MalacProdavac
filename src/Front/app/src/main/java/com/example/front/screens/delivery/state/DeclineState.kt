package com.example.front.screens.delivery.state

import com.example.front.model.response.SuccessBoolean

data class DeclineState (
    var isLoading: Boolean = true,
    var decline: SuccessBoolean? = null,
    var error: String = ""
)
package com.example.front.screens.notification.state

import com.example.front.model.response.SuccessBoolean

data class UserRateState (
    var isLoading: Boolean = true,
    var userRate: SuccessBoolean? = null,
    var error: String = ""
)
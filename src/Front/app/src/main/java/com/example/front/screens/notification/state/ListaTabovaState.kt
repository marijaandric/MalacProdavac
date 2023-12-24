package com.example.front.screens.notification.state

import com.example.front.model.response.SuccessBoolean

data class ListaTabovaState (
    var isLoading: Boolean = true,
    var lista: List<Int> = emptyList(),
    var error: String = ""
)
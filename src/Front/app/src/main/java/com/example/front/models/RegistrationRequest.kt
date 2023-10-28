package com.example.front.models

data class RegistrationRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val address: String,
    val roleId: Int
)

data class RegistrationResponse(val userId: Int)

package com.example.front.api

import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {
    @Headers("Content-Type: application/json")
    @POST("back/Auth/Login")
    suspend fun getLoginInfo(
        @Body login:LoginDTO
    ): Response<Int>

    @Headers("Content-Type: application/json")
    @POST("back/Auth/Register")
    suspend fun register(
        @Body registrationRequest: RegistrationRequest
    ): Response<Int>
}
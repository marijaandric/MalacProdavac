package com.example.front

import com.example.front.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("back/Auth/Register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<Int>
}

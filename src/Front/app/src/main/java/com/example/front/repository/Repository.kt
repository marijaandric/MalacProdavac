package com.example.front.repository

import com.example.front.api.RetrofitInstance
import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import retrofit2.Response

class Repository {
    suspend fun getLogin(login: LoginDTO): Response<Int> {
        return RetrofitInstance.api.getLoginInfo(login)
    }
    suspend fun register(registrationRequest: RegistrationRequest): Response<Int> {
        return RetrofitInstance.api.register(registrationRequest)
    }
}
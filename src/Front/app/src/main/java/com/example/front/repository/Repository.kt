package com.example.front.repository

import com.example.front.api.RetrofitInstance
import com.example.front.model.CategoriesDTO
import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import com.example.front.model.LoginResponse
import retrofit2.Response

class Repository {
    suspend fun getLogin(login: LoginDTO): Response<LoginResponse> {
        return RetrofitInstance.api.getLoginInfo(login)
    }
    suspend fun register(registrationRequest: RegistrationRequest): Response<Int> {
        return RetrofitInstance.api.register(registrationRequest)
    }
    suspend fun getCategories(): Response<List<CategoriesDTO>>{
        return RetrofitInstance.api.getCategories()
    }
}
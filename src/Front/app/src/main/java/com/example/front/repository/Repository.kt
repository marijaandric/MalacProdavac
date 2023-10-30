package com.example.front.repository

import com.example.front.api.RetrofitInstance
import com.example.front.model.CategoriesDTO
import com.example.front.model.LoginDTO
import retrofit2.Response

class Repository {
    suspend fun getLogin(login: LoginDTO): Response<Int> {
        return RetrofitInstance.api.getLoginInfo(login)
    }

    suspend fun getCategories(): Response<List<CategoriesDTO>>{
        return RetrofitInstance.api.getCategories()
    }
}
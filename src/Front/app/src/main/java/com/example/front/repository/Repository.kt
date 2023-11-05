package com.example.front.repository

import com.example.front.api.RetrofitInstance
import com.example.front.model.CategoriesDTO
import com.example.front.model.ChosenCategoriesDTO
import com.example.front.model.HomeProduct
import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import com.example.front.model.LoginResponse
import com.example.front.model.ShopDTO
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
    suspend fun postCategories(categories: ChosenCategoriesDTO): Response<Boolean>{
        return RetrofitInstance.api.saveChosenCategories(categories)
    }
    suspend fun getHomeProducts(id:Int):Response<List<HomeProduct>>{
        return RetrofitInstance.api.getHomeProducts(id)
    }
    suspend fun getHomeShops(id:Int):Response<List<ShopDTO>>{
        return RetrofitInstance.api.getHomeShops(id)
    }
}
package com.example.front.repository

import com.example.front.api.Api
import com.example.front.api.RetrofitInstance
import com.example.front.model.CategoriesDTO
import com.example.front.model.ChosenCategoriesDTO
import com.example.front.model.HomeProduct
import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import com.example.front.model.LoginResponse
import com.example.front.model.ShopDTO
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val api: Api) {
    suspend fun getLogin(login: LoginDTO): Response<LoginResponse> {
        return api.getLoginInfo(login)
    }

    suspend fun register(registrationRequest: RegistrationRequest): Response<LoginResponse> {
        return api.register(registrationRequest)
    }

    suspend fun getCategories(): Response<List<CategoriesDTO>> {
        return api.getCategories()
    }

    suspend fun postCategories(categories: ChosenCategoriesDTO): Response<Boolean> {
        return api.saveChosenCategories(categories)
    }

    suspend fun getHomeProducts(id: Int): Response<List<HomeProduct>> {
        return api.getHomeProducts(id)
    }

    suspend fun getHomeShops(id: Int): Response<List<ShopDTO>> {
        return api.getHomeShops(id)
    }
}
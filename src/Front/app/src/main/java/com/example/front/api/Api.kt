package com.example.front.api

import com.example.front.model.CategoriesDTO
import com.example.front.model.ChosenCategoriesDTO
import com.example.front.model.HomeProduct
import com.example.front.model.LoginDTO
import com.example.front.model.RegistrationRequest
import com.example.front.model.LoginResponse
import com.example.front.model.ShopDTO
import com.example.front.model.product.ProductInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface Api {
    @Headers("Content-Type: application/json")
    @POST("back/Auth/Login")
    suspend fun getLoginInfo(
        @Body login:LoginDTO
    ): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("back/Auth/Register")
    suspend fun register(
        @Body registrationRequest: RegistrationRequest
    ): Response<LoginResponse>

    @GET("back/Home/GetCategories")
    suspend fun getCategories(

    ): Response<List<CategoriesDTO>>

    @POST("/back/Home/SaveChosenCategories")
    suspend fun saveChosenCategories(
        @Body chosenCategories:ChosenCategoriesDTO
    ): Response<Boolean>

    @GET("back/Home/GetHomeProducts")
    suspend fun getHomeProducts(
        @Query("id") id: Int
    ): Response<List<HomeProduct>>

    @GET("back/Home/GetHomeShops")
    suspend fun getHomeShops(
        @Query("id") id: Int
    ): Response<List<ShopDTO>>

    @Headers("Content-Type: application/json")
    @GET("/back/Product/ProductDetails")
    suspend fun getProductDetails(
        @Query("productId") productID: Int,
        @Query("userId") userID: Int
    ): Response<ProductInfo>
}
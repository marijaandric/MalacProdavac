package com.example.front.api

import com.example.front.model.DTO.CategoriesDTO
import com.example.front.model.DTO.ChosenCategoriesDTO
import com.example.front.model.DTO.HomeProductDTO
import com.example.front.model.DTO.LoginDTO
import com.example.front.model.DTO.ProductDTO
import com.example.front.model.request.RegistrationRequest
import com.example.front.model.response.LoginResponse
import com.example.front.model.DTO.ShopDTO
import com.example.front.model.DTO.ShopDetailsDTO
import com.example.front.model.DTO.ShopPagesDTO
import com.example.front.model.DTO.ToggleLikeDTO
import com.example.front.model.product.ProductInfo
import com.example.front.model.product.ProductReviewUserInfo
import com.example.front.model.user.MyProfileDTO
import com.example.front.model.user.UserEditDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface Api {
    @Headers("Content-Type: application/json")
    @POST("back/Auth/Login")
    suspend fun getLoginInfo(
        @Body login: LoginDTO
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
        @Body chosenCategories: ChosenCategoriesDTO
    ): Response<Boolean>

    @GET("back/Home/GetHomeProducts")
    suspend fun getHomeProducts(
        @Query("id") id: Int
    ): Response<List<HomeProductDTO>>

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

    @GET("back/User/MyProfile")
    suspend fun getMyProfileInfo(
        @Query("userId") productID: Int,
    ): Response<MyProfileDTO>

    @PUT("back/Auth/Edit")
    suspend fun editUserProfile(
        @Body data : UserEditDTO
    ):Response<LoginResponse>

    @POST("back/Shop/ToggleLike")
    suspend fun toggleLike(
        @Query("shopId") shopId : Int,
        @Query("userId") userId : Int,
    ): Response<ToggleLikeDTO>

    @GET("/back/Product/ProductReviews")
    suspend fun getReviewsForProduct(
        @Query("productId") productId: Int,
        @Query("page") page: Int
    ): Response<List<ProductReviewUserInfo>>

    @GET("back/Shop/GetShops")
    suspend fun getShops(
        @Query("userId") userId: Int,
        @Query("categories") categories: List<Int>?,
        @Query("rating") rating: Int?,
        @Query("open") open: Boolean?,
        @Query("range") range: Int?,
        @Query("location") location: String?,
        @Query("sort") sort: Int,
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("favorite") favorite: Boolean?,
        @Query("currLat") currLat: Float?,
        @Query("currLong") currLong: Float?
    ):Response<List<ShopDTO>>

    @PUT("/back/Auth/FCMTokenSave")
    suspend fun saveFCMToken(
        @Query("userId") userID: Int,
        @Query("token") token: String
    ): Response<Boolean>

    @GET("back/Shop/ShopPages")
    suspend fun getShopPages():Response<ShopPagesDTO>

    @GET("back/Shop/ShopDetails")
    suspend fun getShopDetails(
        @Query("shopId") shopId: Int,
        @Query("userId") userId: Int
    ):Response<ShopDetailsDTO>

    @GET("back/Product/GetProducts")
    suspend fun getProducts(
        @Query("userId") userId: Int,
        @Query("categories") categories: List<Int>?,
        @Query("rating") rating: Int?,
        @Query("open") open: Boolean?,
        @Query("range") range: Int?,
        @Query("location") location: String?,
        @Query("sort") sort: Int,
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("specificShopId") specificShopId: Int,
        @Query("favorite") favorite: Boolean?,
        @Query("currLat") currLat: Float?,
        @Query("currLong") currLong: Float?
    ):Response<ProductDTO>
}
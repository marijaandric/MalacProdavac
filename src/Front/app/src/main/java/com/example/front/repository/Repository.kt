package com.example.front.repository

import com.example.front.api.Api
import com.example.front.model.DTO.CategoriesDTO
import com.example.front.model.DTO.ChosenCategoriesDTO
import com.example.front.model.DTO.HomeProductDTO
import com.example.front.model.DTO.LoginDTO
import com.example.front.model.request.RegistrationRequest
import com.example.front.model.response.LoginResponse
import com.example.front.model.DTO.ShopDTO
import com.example.front.model.DTO.ToggleLikeDTO
import com.example.front.model.product.ProductInfo
import com.example.front.model.product.ProductReviewUserInfo
import com.example.front.model.user.MyProfileDTO
import com.example.front.model.user.UserEditDTO
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

    suspend fun getHomeProducts(id: Int): Response<List<HomeProductDTO>> {
        return api.getHomeProducts(id)
    }

    suspend fun getHomeShops(id: Int): Response<List<ShopDTO>> {
        return api.getHomeShops(id)
    }

    suspend fun getProductInfo(productId: Int, userId:Int): Response<ProductInfo> {
        return api.getProductDetails(productId,userId)
    }

    suspend fun getMyProfileInfo(userId:Int): Response<MyProfileDTO>{
        return api.getMyProfileInfo(userId)
    }

    suspend fun editProfile(editDTO: UserEditDTO): Response<LoginResponse>{
        return api.editUserProfile(editDTO)
    }

    suspend fun toggleLike(shopId:Int, userId: Int): Response<ToggleLikeDTO>{
        return api.toggleLike(shopId,userId)
    }

    suspend fun getReviewsForProduct(productId: Int, page: Int): Response<List<ProductReviewUserInfo>> {
        return api.getReviewsForProduct(productId,page)
    }

    suspend fun getShops(userId: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean): Response<List<ShopDTO>>{
        return api.getShops(userId,categories,rating,open,range,location,sort,search,page,favorite)
    }
    suspend fun saveFCMToken(userID:Int, token: String) : Response<Boolean>{
        return api.SaveFCMToken(userID,token)
    }
}
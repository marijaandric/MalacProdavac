package com.example.front.repository

import com.example.front.model.DTO.CheckAvailabilityResDTO
import com.example.front.model.product.ProductInCart
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun getCartProducts(): Flow<List<ProductInCart>>
    suspend fun insertProduct(product: ProductInCart)
    suspend fun updateProduct(product: ProductInCart)
    suspend fun deleteProduct(id: Int)
    fun getUniqueShops(): Flow<List<Int>>
    suspend fun updateProductsAvailability(response: List<CheckAvailabilityResDTO>)
}
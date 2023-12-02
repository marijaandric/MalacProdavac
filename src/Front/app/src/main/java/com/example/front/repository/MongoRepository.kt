package com.example.front.repository

import com.example.front.model.product.ProductInCart
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface MongoRepository {
    fun getCartProducts(): Flow<List<ProductInCart>>
    suspend fun insertProduct(product: ProductInCart)
    suspend fun updateProduct(product: ProductInCart)
    suspend fun deleteProduct(id: Int)
    fun getUniqueShops(): List<String>
}
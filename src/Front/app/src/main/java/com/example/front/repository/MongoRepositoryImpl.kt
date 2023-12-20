package com.example.front.repository

import android.util.Log
import com.example.front.model.DTO.CheckAvailabilityResDTO
import com.example.front.model.product.ProductInCart
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MongoRepositoryImpl(val realm: Realm) : MongoRepository {
    override fun getCartProducts(): Flow<List<ProductInCart>> {
        return realm.query<ProductInCart>().asFlow().map { it.list }
    }

    override suspend fun insertProduct(product: ProductInCart) {
        realm.write { copyToRealm(product) }
    }

    override suspend fun updateProduct(product: ProductInCart) {
        realm.write {
            val existingProduct = query<ProductInCart>(
                query = "id == $0 AND size == $1", product.id, product.size).first().find()
            println("PROIZVODDD::: " + existingProduct?.name.toString())
            if (existingProduct != null) {
                existingProduct.name = product.name
                existingProduct.price = product.price
                existingProduct.quantity = product.quantity
                existingProduct.shopId = product.shopId
                existingProduct.shopName = product.shopName
                existingProduct.image = product.image
                existingProduct.metric = product.metric
                existingProduct.size = product.size
                existingProduct.sizeId = product.sizeId
                existingProduct.available = product.available
            } else {
                copyToRealm(product)
            }
        }
    }

    override suspend fun deleteProduct(id: Int) {
        realm.write {
            val product = query<ProductInCart>(query = "id == $0", id).first().find()
            try {
                product?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepositoryImpl", "${e.message}")
            }
        }
    }

    override fun getUniqueShops(): Flow<List<Int>> {
        return realm.query<ProductInCart>()
            .asFlow()
            .map { it.list }
            .map { products ->
                products.map { it.shopId }.distinct()
            }
    }

    override suspend fun updateProductsAvailability(response: List<CheckAvailabilityResDTO>) {
        realm.write {
            response.forEach { product ->
                val existingProduct = query<ProductInCart>(
                    query = "id == $0 AND size == $1", product.id, product.size
                ).first().find()

                if (existingProduct != null) {
                    existingProduct.available = product.available
                }
            }
        }
    }

    override suspend fun clearAllData() {
        realm.write {
            deleteAll()
        }
    }
}
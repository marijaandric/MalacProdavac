package com.example.front.repository

import android.util.Log
import com.example.front.model.product.ProductInCart
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class MongoRepositoryImpl(val realm: Realm) : MongoRepository {
    override fun getData(): Flow<List<ProductInCart>> {
        return realm.query<ProductInCart>().asFlow().map { it.list }
    }

    override suspend fun insertProduct(product: ProductInCart) {
        realm.write { copyToRealm(product) }
    }

    override suspend fun updateProduct(product: ProductInCart) {
        realm.write {
            val queriedPerson = query<ProductInCart>(query = "id == $0", product.id).first().find()
            queriedPerson?.name = product.name
        }
    }

    override suspend fun deleteProduct(id: ObjectId) {
        realm.write {
            val product = query<ProductInCart>(query = "id == $0", id).first().find()
            try {
                product?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepositoryImpl", "${e.message}")
            }
        }
    }
}
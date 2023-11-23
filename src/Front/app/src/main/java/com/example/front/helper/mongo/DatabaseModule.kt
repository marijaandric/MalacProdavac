package com.example.front.helper.mongo

import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.MongoRepositoryImpl
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                ProductInCart::class
            )
        )
            .compactOnLaunch()
//            .initialData()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideMongoRepository(realm: Realm): MongoRepository {
        return MongoRepositoryImpl(realm = realm)
    }
}
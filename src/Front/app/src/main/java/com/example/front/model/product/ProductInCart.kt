package com.example.front.model.product

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ProductInCart : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var price: Float = 0.0f
    var quantity: Double = 0.0
    var shopId: Int = 0
    var shopName: String = ""
    var image: String = ""
    var metric: String = ""
}
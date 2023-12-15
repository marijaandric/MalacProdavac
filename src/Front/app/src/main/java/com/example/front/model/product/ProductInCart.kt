package com.example.front.model.product

import io.realm.kotlin.types.RealmObject

class ProductInCart : RealmObject {
//    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var price: Double = 0.0
    var quantity: Int = 0
    var shopId: Int = 0
    var shopName: String = ""
    var image: String = ""
    var metric: String = ""
    var size: String = "None"
    var available: Int = 0
}
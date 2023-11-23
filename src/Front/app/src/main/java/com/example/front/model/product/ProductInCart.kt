package com.example.front.model.product

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ProductInCart : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var price: Double = 0.0
    var count: Double = 0.0
    var delivery: Boolean = false
}
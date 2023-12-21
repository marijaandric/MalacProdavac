package com.example.front.model.user

import io.realm.kotlin.types.RealmObject

class CreditCardModel : RealmObject {
    var cardNumber: String = ""
    var nameOnCard: String = ""
    var expDate: String = ""
    var cvv: String = ""
}
package com.example.front.viewmodels.checkout

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.NewOrder
import com.example.front.model.DTO.ShopDetailsCheckoutDTO
import com.example.front.model.user.CreditCardModel
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.viewmodels.cart.CartState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {

    private val _state = mutableStateOf<List<Int>>(emptyList())
    val state: State<List<Int>> = _state

    private val _shopsForCheckout = mutableStateOf<List<ShopDetailsCheckoutDTO>>(emptyList())
    val shopsForCheckout: State<List<ShopDetailsCheckoutDTO>> = _shopsForCheckout

    private val _stateProducts = mutableStateOf(CartState())
    val stateProducts: State<CartState> = _stateProducts

    private val _creditCards = mutableStateOf<List<CreditCardModel>>(emptyList())
    val creditCards: State<List<CreditCardModel>> = _creditCards

    suspend fun getCheckoutData(totalsByShop: Map<Int, Double>) {
        try {
            mongoRepository.getUniqueShops().collect() { shops ->
                _state.value = shops
                fetchShopsForCheckout(state.value, totalsByShop)
            }
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error fetching checkout data: ${e.message}")
        }
    }

    private suspend fun fetchShopsForCheckout(shopsIds: List<Int>, totalsByShop: Map<Int, Double>) {
        try {
            val response = repository.getShopsForCheckout(shopsIds)
            if (response.isSuccessful) {
                _shopsForCheckout.value = response.body() ?: emptyList()
                _shopsForCheckout.value.forEach { shop ->
                    shop.total = totalsByShop[shop.id] ?: 0.0
                }
                getCartProducts()
            } else {
                Log.e("CheckoutViewModel", "Error fetching shop details checkout: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error fetching shop details checkout: ${e.message}")
        }
    }

    private suspend fun getCartProducts() {
        try {
            mongoRepository.getCartProducts().collect() {cartProducts ->
                _stateProducts.value = _stateProducts.value.copy(
                    isLoading = false,
                    products = cartProducts
                )
            }
        } catch (e: Exception) {
            _stateProducts.value = _stateProducts.value.copy(
                isLoading = false,
            )
        }
    }

    fun updateSelfPickup(shopId: Int, value: Boolean) {
        val updatedShops = _shopsForCheckout.value.toMutableList()
        val shopIndex = updatedShops.indexOfFirst { it.id == shopId }
        if (shopIndex != -1) {
            updatedShops[shopIndex] = updatedShops[shopIndex].copy(selfpickup = value)
            _shopsForCheckout.value = updatedShops
        }
    }

    suspend fun insertOrders(orders: List<NewOrder>): Boolean {
        val response = repository.insertOrders(orders)
        ////

        val gson = Gson()
        val jsonRequest = gson.toJson(orders)
        println("JSON zahtev: $jsonRequest")


        println("RESPONSE:  BODY: ${response.body().toString()}")
        println("RESPONSE:  MESSAGE: ${response.message()}")

        if (response.isSuccessful) {
            ////brisanje korpe
            mongoRepository.clearAllData()
        }
        return response.isSuccessful
    }

    suspend fun insertCreditCard(card: CreditCardModel) {
        try {
            mongoRepository.insertCreditCard(card)
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error inserting credit card: ${e.message}")
        }
    }
    suspend fun getAllCreditCards() {
        try {
            mongoRepository.getAllCreditCards().collect() {creditCards ->
                _creditCards.value = creditCards
                println("CREDITNE KARTICE: ${this.creditCards.value}")
            }
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error fetching credit cards: ${e.message}")
        }
    }
}
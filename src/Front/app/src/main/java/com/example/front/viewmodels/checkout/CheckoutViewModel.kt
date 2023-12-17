package com.example.front.viewmodels.checkout

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.ShopDetailsCheckoutDTO
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.viewmodels.cart.CartState
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

    suspend fun getCartProducts() {
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
            } else {
                Log.e("CheckoutViewModel", "Error fetching shop details checkout: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error fetching shop details checkout: ${e.message}")
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
}
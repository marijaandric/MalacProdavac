package com.example.front.viewmodels.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.CheckAvailabilityReqDTO
import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
): ViewModel() {

    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    suspend fun getCartProducts() {
        try {
            mongoRepository.getCartProducts().collect() {cartProducts ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    products = cartProducts
                )
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
            )
        }
    }

    suspend fun removeProductFromCart(id: Int) {
        mongoRepository.deleteProduct(id)
        getCartProducts()
    }

    suspend fun isAvailable(cardProducts: List<ProductInCart>) {
        var products = cardProducts.map {
            CheckAvailabilityReqDTO(it.id, it.size, it.quantity)
        }
        println("LISTA: $products")
////
        val gson = Gson()
        val jsonRequest = gson.toJson(products)
        println("JSON zahteva: $jsonRequest")

        try {
            val response = repository.checkProductsAvailability(products)
            if (response.isSuccessful) {
                println("RADIIII")
//                _state.value.products.forEach { product ->
//                    println("PROIZVOD: $product")
//                    val nesto = response.body()?.find {
//                        it.id == product.id
//                    }?.available
//                    println("PROSAO")
//                    mongoRepository.updateProduct()
//                    product.available = nesto ?: product.available
//                }
//                if (response.body() != null) {
                    mongoRepository.updateProductsAvailability(response.body()!!)
//                }
            } else {
                println(response.message())
                println(response.code())
                println(response.body())
            }
            println(response.isSuccessful)
        } catch (e: Exception) {
            println("Exception in isAvailable(): $e")
        }
    }
}

data class CartState(
    val isLoading: Boolean = true,
    val products: List<ProductInCart> = emptyList()
)
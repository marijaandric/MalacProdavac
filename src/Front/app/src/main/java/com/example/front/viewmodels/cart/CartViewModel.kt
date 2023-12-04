package com.example.front.viewmodels.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
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
}

data class CartState(
    val isLoading: Boolean = true,
    val products: List<ProductInCart> = emptyList()
)
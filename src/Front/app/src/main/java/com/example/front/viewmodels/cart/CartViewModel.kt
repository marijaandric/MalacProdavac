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
    private val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
): ViewModel() {

    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    suspend fun getCartProducts() {
        // Implementirajte logiku za dohvat proizvoda iz korpe iz repository-ja
        // Na primer, možete koristiti repository metodu poput getCartProducts()
        // koja vraća listu proizvoda u korpi.
        // Ažurirajte _state kada podaci budu dostupni.

        // Primer:
        try {
            mongoRepository.getCartProducts().collect() {cartProducts ->
                // Pretpostavljeni naziv metode
                _state.value = _state.value.copy(
                    isLoading = false,
                    products = cartProducts
                )
            }
        } catch (e: Exception) {
            // Handle error if needed
            _state.value = _state.value.copy(
                isLoading = false,
                // Možete dodati dodatna polja u CartState za rukovanje greškama
            )
        }
    }

}

data class CartState(
    val isLoading: Boolean = true,
    val products: List<ProductInCart> = emptyList()
)
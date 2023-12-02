package com.example.front.viewmodels.checkout

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {

    private val _state = mutableStateOf<List<String>>(emptyList())
    val state: State<List<String>> = _state

    fun getCheckoutData() {
        viewModelScope.launch {
            try {
                val checkoutData = mongoRepository.getUniqueShops()
                _state.value = checkoutData
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "Error fetching checkout data: ${e.message}")
            }
        }
    }
}

data class CartState(
    val isLoading: Boolean = true,
    val products: List<ProductInCart> = emptyList()
)
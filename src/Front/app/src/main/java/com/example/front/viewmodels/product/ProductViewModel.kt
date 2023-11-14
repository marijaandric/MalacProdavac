package com.example.front.viewmodels.product

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.product.ProductProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(ProductProductState())
    var state: State<ProductProductState> = _state;

    suspend fun getProductInfo(productID: Int, userID: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductInfo(productID, userID)
                if (response.isSuccessful) {
                    val productInfo = response.body()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        product = productInfo
                    )
                } else {

                }
            } catch (e: Exception) {
                Log.d("Eroorrrrr", e.message.toString())
            }
        }
    }
}
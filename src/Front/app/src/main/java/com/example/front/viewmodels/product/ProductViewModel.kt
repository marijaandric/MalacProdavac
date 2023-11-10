package com.example.front.viewmodels.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInfo
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _productInfo = MutableStateFlow<ProductInfo?>(null)
    val productInfo: Flow<ProductInfo?> = _productInfo

    suspend fun getProductInfo(productID: Int, userID: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductInfo(productID, userID)
                Log.d("TAAAG", response.toString())
                Log.d("TAAAG", response.body().toString())
                if (response.isSuccessful) {
                    val productInfo = response.body()
                    _productInfo.value = productInfo
                } else {
                    // Handle the case where the response is not successful
                    // For example, you can check response.code() for the HTTP status code
                    Log.d("TAAAG", "Request failed with code: ${response.code()}")
                    Log.d("TAAAG", "Error response body: ${response.body()}")
                }
            } catch (e: Exception) {
                // Handle exceptions, e.g., network issues or JSON parsing errors
                //println("Request failed with code: ${response.code()}")
                Log.d("Eroorrrrr",e.message.toString())
            }
        }
    }
}
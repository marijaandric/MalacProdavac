package com.example.front.viewmodels.product

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInfo
import com.example.front.model.product.ProductReviewUserInfo
import com.example.front.repository.Repository
import com.example.front.screens.product.ProductProductState
import com.example.front.screens.product.ReviewProductState
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

    private val _stateReview = mutableStateOf(ReviewProductState())
    var stateReview: State<ReviewProductState> = _stateReview;


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

    suspend fun getReviewsForProduct(productID: Int, pageNumber: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getReviewsForProduct(productID, pageNumber)
                if (response.isSuccessful) {
                    val reviewsResponse = response.body()
                    _stateReview.value = _stateReview.value.copy(
                        isLoading = false,
                        reviews = _stateReview.value.reviews.orEmpty() + (reviewsResponse ?: emptyList())
                    )
                } else {
                    // Handle error if needed
                }
            } catch (e: Exception) {
                Log.d("Eroorrrrr", e.message.toString())
            }
        }
    }

    fun resetReviewState() {
        _stateReview.value = ReviewProductState()
    }


    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}
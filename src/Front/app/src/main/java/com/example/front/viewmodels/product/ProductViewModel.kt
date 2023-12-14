package com.example.front.viewmodels.product

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.screens.product.ProductProductState
import com.example.front.screens.product.ReviewProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
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
                        reviews = _stateReview.value.reviews.orEmpty() + (reviewsResponse
                            ?: emptyList())
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


    suspend fun getUserId(): Int? {
        return dataStoreManager.getUserIdFromToken()
    }

    fun addToCart(
        productID: Int,
        name: String,
        price: Float,
        quantity: Int,
        shopId: Int,
        shopName: String,
        image: String,
        metric: String,
        size: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productInCart = ProductInCart()
                productInCart.id = productID
                productInCart.name = name
                productInCart.price = price
                productInCart.quantity = quantity.toDouble()
                productInCart.shopId = shopId
                productInCart.shopName = shopName
                productInCart.image = image
                productInCart.metric = metric
                productInCart.size = size
                productInCart.available = quantity.toDouble()

                mongoRepository.updateProduct(productInCart)
            } catch (e: Exception) {
                Log.e("addToCart", "Greska prilikom dodavanja proizvoda u korpu: ${e.message}")
            }
        }
    }
}
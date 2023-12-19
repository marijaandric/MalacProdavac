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
import com.example.front.screens.myshop.state.ImageState
import com.example.front.screens.product.ProductProductState
import com.example.front.screens.product.ReviewProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    private val _stateimage = mutableStateOf(ImageState())
    var stateimage: State<ImageState> = _stateimage;


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
                Log.d("Error", e.message.toString())
            }
        }
    }

    suspend fun getReviewsForProduct(productID: Int, pageNumber: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getReviewsForProduct(productID, pageNumber)
                Log.d("Response", response.toString())
                if (response.isSuccessful) {
                    val reviewsResponse = response.body()
                    _stateReview.value = _stateReview.value.copy(
                        isLoading = false,
                        reviews = reviewsResponse
                    )
                } else {
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    fun ChangePage(currentPage: Int) {
        viewModelScope.launch {
            _state.value.product!!.productId?.let { getReviewsForProduct(it, currentPage) }
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
        size: String,
        sizeId : Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productInCart = ProductInCart()
                productInCart.id = productID
                productInCart.name = name
                productInCart.price = price.toDouble()
                productInCart.quantity = quantity
                productInCart.shopId = shopId
                productInCart.shopName = shopName
                productInCart.image = image
                productInCart.metric = metric
                productInCart.size = size
                productInCart.sizeId = sizeId
                productInCart.available = quantity

                mongoRepository.updateProduct(productInCart)
            } catch (e: Exception) {
                Log.e("addToCart", "Greska prilikom dodavanja proizvoda u korpu: ${e.message}")
            }
        }
    }
    fun uploadImages(type: Int, id: Int, imageParts: List<MultipartBody.Part>) {
        viewModelScope.launch {
            val uploadResults = imageParts.map { imagePart ->
                try {
                    val response = repository.uploadImage2(type, id, imagePart)
                    if (response.isSuccessful) {
                        Result.success(response.body())
                    } else {
                        Result.failure(Exception("Upload failed with error code: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            val successfulUploads = uploadResults.count { it.isSuccess }
            val failedUploads = uploadResults.size - successfulUploads

            _stateimage.value = _stateimage.value.copy(
                isLoading = false,
                image = null,
                error = if (failedUploads > 0) "Some uploads failed" else ""
            )
        }
    }

}
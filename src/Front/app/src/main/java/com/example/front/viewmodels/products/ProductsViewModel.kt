package com.example.front.viewmodels.products

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.screens.product.ProductProductState
import com.example.front.screens.product.ReviewProductState
import com.example.front.screens.products.state.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {

    private val _state = mutableStateOf(ProductsState())
    var state: State<ProductsState> = _state;

    private val _stateFav = mutableStateOf(ProductsState())
    var stateFav: State<ProductsState> = _stateFav;

    private val _stateReview = mutableStateOf(ReviewProductState())
    var stateReview: State<ReviewProductState> = _stateReview;


    suspend fun getProducts(userID: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean,currLat:Float?, currLong:Float?) {
        viewModelScope.launch {
            try {
                val response = repository.getProducts(userID,categories,rating,open,range,location,sort,search,page,null,favorite, currLat, currLong)
                Log.d("PRODUCTS",response.toString())
                if (response.isSuccessful) {
                    val products= response.body()
                    if(!favorite) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = products,
                            error = ""
                        )
                    }
                    else{
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            products = products,
                            error = ""
                        )
                    }
                } else {
                    if(!favorite) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = emptyList(),
                            error = "NotFound"
                        )
                    }
                    else{
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            products = emptyList(),
                            error = "NotFound"
                        )
                    }
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
}
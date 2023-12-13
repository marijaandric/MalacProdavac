package com.example.front.viewmodels.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.product.ProductInCart
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.screens.home.states.HomeProductsState
import com.example.front.screens.home.states.HomeShopState
import com.example.front.screens.home.states.ToggleLikeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {


    private val _state = mutableStateOf(HomeProductsState())
    var state : State<HomeProductsState> = _state;

    private val _stateShop = mutableStateOf(HomeShopState())
    var stateShop : State<HomeShopState> = _stateShop;

    private val _stateLike = mutableStateOf(ToggleLikeState())
    var stateLike : State<ToggleLikeState> = _stateLike;



    fun getHomeProducts(userId: Int?)
    {
        viewModelScope.launch {
            try{
                val id = dataStoreManager.getUserIdFromToken()
                if(id != null)
                {
                    val response = userId?.let { repository.getHomeProducts(id) }

                    if (response != null) {
                        if (response.isSuccessful) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                products = response.body()
                            )
                        }
                    }
                }
            }
            catch (e:Exception)
            {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun getHomeShops(userId: Int?)
    {
        viewModelScope.launch {
            val id = dataStoreManager.getUserIdFromToken()
            if(id != null) {
                try{
                    val response = userId?.let { repository.getHomeShops(id) }

                    if (response != null) {
                        if (response.isSuccessful) {
                            _stateShop.value = _stateShop.value.copy(
                                isLoading = false,
                                shops = response.body()
                            )
                        }
                    }
                }
                catch (e:Exception)
                {
                    _stateShop.value.error = e.message.toString()
                }
            }
        }
    }

    fun changeLikedState(shopId:Int)
    {
        viewModelScope.launch {
            val userId = dataStoreManager.getUserIdFromToken()
            if(userId != null) {
                try{
                    val response = repository.toggleLike(shopId,userId)
                    Log.d("Toggle RESPONSE",response.toString())

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        _stateLike.value = _stateLike.value.copy(
                            isLoading = false,
                            success = response.body()
                        )
                    }
                }
                catch (e:Exception)
                {
                    _stateLike.value.error = e.message.toString()
                }
            }
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }

    fun updateLikeStatus(index: Int, isLiked: Boolean) {
        val currentState = stateShop.value
        currentState?.shops?.get(index)?.let { shopState ->
            val updatedShop = shopState.copy(liked = isLiked)
            val updatedShops = currentState.shops!!.toMutableList()
            updatedShops[index] = updatedShop
            _stateShop.value = currentState.copy(shops = updatedShops)
        }
        Log.d("Promenjen like status? ", _stateShop.value.toString())
    }

    fun addToCart(
        productID: Int,
        name: String,
        price: Float,
        quantity: Int,
        shopName: String,
        image: String,
        metric: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productInCart = ProductInCart()
                productInCart.id = productID
                productInCart.name = name
                productInCart.price = price
                productInCart.quantity = quantity.toDouble()
                productInCart.shopName = shopName
                productInCart.metric = metric

                mongoRepository.updateProduct(productInCart)
            } catch (e: Exception) {
                Log.e("addToCart", "Greska prilikom dodavanja proizvoda u korpu: ${e.message}")
            }
        }
    }

}
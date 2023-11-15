package com.example.front.viewmodels.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.home.HomeProductsState
import com.example.front.screens.home.HomeShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(HomeProductsState())
    var state : State<HomeProductsState> = _state;

    private val _stateShop = mutableStateOf(HomeShopState())
    var stateShop : State<HomeShopState> = _stateShop;


    fun getHomeProducts(id: Int)
    {
        viewModelScope.launch {
            try{
                val response = repository.getHomeProducts(id)
                Log.d("RESPONSE",response.toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        products = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun getHomeShops(id: Int)
    {
        viewModelScope.launch {
            try{
                val response = repository.getHomeShops(id)
                Log.d("RESPONSE",response.toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _stateShop.value = _stateShop.value.copy(
                        isLoading = false,
                        shops = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _stateShop.value.error = e.message.toString()
            }
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }

}
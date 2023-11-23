package com.example.front.viewmodels.oneshop

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.shop.state.ShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneShopViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(ShopState())
    var state: State<ShopState> = _state;

    suspend fun getShopDetails(userId: Int, shopId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getShopDetails(userId, shopId)
                if (response.isSuccessful) {
                    val shopp = response.body()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        shop = shopp
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }
}
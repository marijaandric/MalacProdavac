package com.example.front.viewmodels.orderinfo

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.screens.home.states.HomeProductsState
import com.example.front.screens.home.states.HomeShopState
import com.example.front.screens.home.states.ToggleLikeState
import com.example.front.screens.order.state.OrderInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {


    private val _state = mutableStateOf(OrderInfoState())
    var state: State<OrderInfoState> = _state;

    fun getOrderInfo(oderId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getOrderInfo(oderId)
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        orderInfo = response.body(),
                        error = ""
                    )
                }
                else{
                    _state.value = _state.value.copy(
                        isLoading = false,
                        orderInfo = null,
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
            }
        }
    }
}
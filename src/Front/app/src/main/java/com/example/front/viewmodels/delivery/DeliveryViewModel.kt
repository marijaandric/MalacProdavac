package com.example.front.viewmodels.delivery

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.RouteDetails
import com.example.front.model.DTO.Trip
import com.example.front.repository.Repository
import com.example.front.screens.delivery.state.ReqForDeliveryState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val repository: Repository
) : ViewModel()
{
    private val _state = mutableStateOf(DeliveryRoutes())
    var state: State<DeliveryRoutes> = _state;

    private val _stateReq = mutableStateOf(ReqForDeliveryState())
    var stateReq: State<ReqForDeliveryState> = _stateReq;

    suspend fun getReqForDelivery(userId: Int) {
        try {
            val response = repository.getReqForDelivery(userId)
            Log.d("RES DEL", response.body().toString())
            if (response.isSuccessful) {
                _stateReq.value = _stateReq.value.copy(
                    isLoading = false,
                    req = response.body(),
                    error=""
                )
            } else {
                _stateReq.value = _stateReq.value.copy(
                    isLoading = false,
                    req = null,
                    error="Error"
                )
            }
        } catch (e: Exception) {
            _state.value.error = e.message.toString()
        }
    }

    suspend fun getRouteDetails(userId: Int?) {
        try {
            val response = userId?.let { repository.GetRoutesForDeliveryPerson(it) }
            if (response != null) {
                Log.d("RES DEL", response.body().toString())
            }
            if (response != null) {
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        details = response.body()
                    )
                } else {
                    _state.value.error = response.message()
                }
            }
        } catch (e: Exception) {
            _state.value.error = e.message.toString()
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}

data class DeliveryRoutes(
    var isLoading: Boolean = true,
    var details: List<Trip>? = null,
    var error: String = ""
)
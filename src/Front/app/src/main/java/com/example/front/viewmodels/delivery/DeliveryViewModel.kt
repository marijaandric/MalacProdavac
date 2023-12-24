package com.example.front.viewmodels.delivery

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.NewRoute
import com.example.front.model.DTO.Trip
import com.example.front.repository.Repository
import com.example.front.screens.delivery.state.DeclineState
import com.example.front.screens.delivery.state.ReqForDeliveryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    private val _stateDecline = mutableStateOf(DeclineState())
    var stateDecline: State<DeclineState> = _stateDecline;

    private val _stateAccept = mutableStateOf(DeclineState())
    var stateAccept: State<DeclineState> = _stateAccept;

    fun declineReq(reqId: Int, userId: Int)
    {
        viewModelScope.launch {
            try {
                val response = repository.declineRequest(reqId)
                if (response.isSuccessful) {
                    _stateDecline.value = _stateDecline.value.copy(
                        isLoading = false,
                        decline = response.body(),
                        error=""
                    )
                    getReqForDelivery(userId)
                } else {
                    _stateDecline.value = _stateDecline.value.copy(
                        isLoading = false,
                        decline = null,
                        error="Error"
                    )
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun getReqForDelivery(userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getReqForDelivery(userId)
                Log.d("RES Acc", response.body().toString())
                if (response.isSuccessful) {
                    _stateReq.value = _stateReq.value.copy(
                        isLoading = false,
                        req = response.body(),
                        error = ""
                    )
                } else {
                    _stateReq.value = _stateReq.value.copy(
                        isLoading = false,
                        req = null,
                        error = "Error"
                    )
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun acceptReq(userId: Int, routerId:Int, x: Int) {
        viewModelScope.launch {
            try {
                val response = repository.acceptReq(userId, routerId)
                Log.d("RES ACCC", response.toString())
                if (response.isSuccessful) {
                    _stateAccept.value = _stateAccept.value.copy(
                        isLoading = false,
                        decline = response.body(),
                        error=""
                    )
                    getReqForDelivery(x)
                } else {
                    _stateAccept.value = _stateAccept.value.copy(
                        isLoading = false,
                        decline = null,
                        error="Error"
                    )
                }
            } catch (e: Exception) {
                _stateAccept.value.error = e.message.toString()
            }
        }

    }

    suspend fun getRouteDetails(userId: Int?) {
        try {
            val response = userId?.let { repository.GetRoutesForDeliveryPerson(it) }
            if (response != null) {
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

    suspend fun addNewRoute(startDate: LocalDate, time:String, adresaOd: String, adresaDo: String, cena: Int): Boolean {
        try {
            val model = NewRoute(
                2,
                startDate.format(DateTimeFormatter.ISO_DATE),
                time,
                adresaOd+", Srbija",
                adresaDo+", Srbija",
                cena
            )
            val response = repository.addNewRoute(model)
            println(response)
            println(response.success)
            return response.success
        } catch (e: Exception) {
            println(e.message.toString())
        }
        return false
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
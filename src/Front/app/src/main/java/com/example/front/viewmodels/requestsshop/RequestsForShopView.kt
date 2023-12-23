package com.example.front.viewmodels.requestsshop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.DeliveryPersonDTO
import com.example.front.model.DTO.RequestsForShopDTO
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsForShopViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(RequestsForShopState())
    var state: State<RequestsForShopState> = _state;

    private val _stateDelivery = mutableStateOf(DeliveryPersonRequestState())
    var stateDelivery: State<DeliveryPersonRequestState> = _stateDelivery;

    private val _showDeliveryModal = mutableStateOf(false)
    val showDeliveryModal: State<Boolean> = _showDeliveryModal

    fun getRequestsForShopDTO(id: Int) {
        viewModelScope.launch {
            val response = repository.getRequestsForShopDTO(id)
            if (response.isSuccessful) {
                val requestsForShopDTO = response.body()
                _state.value = _state.value.copy(
                    isLoading = false,
                    requests = requestsForShopDTO!!,
                    error = ""
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = response.message()
                )
            }
        }
    }
    fun getDeliveriesPersonForRequest(id: Int) {
        viewModelScope.launch {
            val response = repository.getDeliveriesPersonForRequest(id)
            if (response.isSuccessful) {
                val persons = response.body()
                _stateDelivery.value = _stateDelivery.value.copy(
                    isLoading = false,
                    persons = persons!!,
                    error = ""
                )
            } else {
                _stateDelivery.value = _stateDelivery.value.copy(
                    isLoading = false,
                    error = response.message()
                )
            }
        }
        _showDeliveryModal.value = true
    }
    fun hideDeliveryModal() {
        _showDeliveryModal.value = false
    }
    fun performRequestWithSelectedPerson(id:Int,deliveryPersonId: Int) {
        viewModelScope.launch {
            repository.chooseDeliveryPerson(id,deliveryPersonId)
            dataStoreManager.getUserIdFromToken()?.let { getRequestsForShopDTO(it) }
        }
        hideDeliveryModal()
    }
}

data class RequestsForShopState(
    var isLoading: Boolean = true,
    var requests: List<RequestsForShopDTO> = listOf(),
    var error: String = ""
)
data class DeliveryPersonRequestState(
    var isLoading: Boolean = true,
    var persons: List<DeliveryPersonDTO> = listOf(),
    var error: String = ""
)
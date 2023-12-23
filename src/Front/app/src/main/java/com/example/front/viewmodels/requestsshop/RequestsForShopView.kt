package com.example.front.viewmodels.requestsshop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.PageCountDTO
import com.example.front.model.DTO.RequestsForShopDTO
import com.example.front.repository.Repository
import com.example.front.screens.shop.state.PostReviewState
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
}

data class RequestsForShopState(
    var isLoading: Boolean = true,
    var requests: List<RequestsForShopDTO> = listOf(),
    var error: String = ""
)
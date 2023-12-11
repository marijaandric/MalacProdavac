package com.example.front.viewmodels.delivery

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.RouteDetails
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val repository: Repository
) : ViewModel() {
    private val _state = mutableStateOf(RouteDetailsState())
    var state: State<RouteDetailsState> = _state;

    suspend fun getRouteDetails(routeID: Int?) {
        try {
            val response = routeID?.let { repository.getRouteDetails(it) }

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
}

data class RouteDetailsState(
    var isLoading: Boolean = true,
    var details: RouteDetails? = null,
    var error: String = ""
)

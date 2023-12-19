package com.example.front.viewmodels.notification

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.NotificationDTO
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val repository: Repository
) : ViewModel(){

    private val _state = mutableStateOf(NotificationState())
    var state: State<NotificationState> = _state;

    private val _statePageCount = mutableStateOf(1)
    var statePageCount: State<Int> = _statePageCount;

    fun getNotifications(userId: Int, type: List<Int>?, page: Int){
        viewModelScope.launch {
            try {
                val response = repository.getNotifications(userId, type, page)

                if (response != null) {
                    if (response.isSuccessful) {
                        _state.value = response.body()?.let {
                            _state.value.copy(
                                isLoading = false,
                                notifications = it
                            )
                        }!!
                        _statePageCount.value = (response.body()!!.size)/3
                    }
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
            }
        }
    }
}

data class NotificationState (
    var isLoading : Boolean = true,
    var notifications : List<NotificationDTO> = listOf(),
    var error : String = ""
)
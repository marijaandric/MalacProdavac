package com.example.front.viewmodels.myshop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.myshop.state.ShopIdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyShopViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {


    private val _state = mutableStateOf(ShopIdState())
    var state: State<ShopIdState> = _state;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow


    fun getShopId(i: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getShopId(i)

                if (response.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        shopId = response.body()
                    )
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
            }
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}
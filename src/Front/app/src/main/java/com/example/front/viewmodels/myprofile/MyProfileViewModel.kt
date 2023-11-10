package com.example.front.viewmodels.myprofile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.home.HomeProductsState
import com.example.front.screens.home.HomeShopState
import com.example.front.screens.userprofile.MyProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(MyProfileState())
    var state: State<MyProfileState> = _state;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow

    fun getMyProfileInfo(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMyProfileInfo(id)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        info = response.body()
                    )
                }
            } catch (e: Exception) {
                _state.value.error = e.message.toString()
                Log.d("Error", _state.value.error)
            }
        }
    }
}
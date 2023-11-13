package com.example.front.viewmodels.myprofile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.user.UserEditDTO
import com.example.front.repository.Repository
import com.example.front.screens.home.HomeProductsState
import com.example.front.screens.home.HomeShopState
import com.example.front.screens.userprofile.EditState
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

    private val _stateEdit = MutableLiveData(EditState())
    var stateEdit : LiveData<EditState> = _stateEdit;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow

    fun getMyProfileInfo(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMyProfileInfo(id)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("USAO SAM", responseBody.toString())

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

    fun editProfile(userEditDTO: UserEditDTO) {
        viewModelScope.launch {
            try {
                val response = repository.editProfile(userEditDTO)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _stateEdit.value = _stateEdit.value!!.copy(
                        isLoading = false,
                        info = response.body()
                    )
                }
                else{
                    _stateEdit.value = _stateEdit.value!!.copy(error = "Error, please check all fields!")
                    Log.d("Error", _stateEdit.value!!.error)
                }
            } catch (e: Exception) {
                _stateEdit.value!!.error = e.message.toString()
                Log.d("Error", _stateEdit.value!!.error)
            }
        }
    }

}
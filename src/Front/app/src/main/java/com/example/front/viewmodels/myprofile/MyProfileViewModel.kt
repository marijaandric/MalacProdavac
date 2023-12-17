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
import com.example.front.screens.myshop.state.ImageState
import com.example.front.screens.userprofile.states.EditState
import com.example.front.screens.userprofile.states.MyProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(MyProfileState())
    var state: State<MyProfileState> = _state;

    private val _stateEdit = MutableLiveData(EditState())
    var stateEdit : LiveData<EditState> = _stateEdit;

    private val _stateimage = mutableStateOf(ImageState())
    var stateimage: State<ImageState> = _stateimage;

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

    fun uploadImage(type: Int, id: Int, imagePart : MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage2(type, id, imagePart)
                Log.d("RES FOR ADD PIC", response.toString())

                if (response.isSuccessful) {
                    _stateimage.value = _stateimage.value.copy(
                        isLoading = false,
                        image = response.body(),
                        error = "CantAdd"
                    )
                } else {
                    _stateimage.value = _stateimage.value.copy(
                        isLoading = false,
                        image = null,
                        error = ""
                    )
                }
            } catch (e: Exception) {
                _stateimage.value = _stateimage.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun changeImage(imagee: String)
    {
        _state.value = _state.value!!.copy(
            info = _state.value.info!!.copy(image = imagee)
        )
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }

    fun inicijalnoStanje(){
        _stateEdit.value = _stateEdit.value!!.copy(
            isLoading = false,
            info = null,
            error = ""
        )
    }

}
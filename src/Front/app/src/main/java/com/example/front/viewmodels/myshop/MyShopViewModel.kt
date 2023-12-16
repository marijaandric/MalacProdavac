package com.example.front.viewmodels.myshop

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.NewShopDTO
import com.example.front.repository.Repository
import com.example.front.screens.myshop.state.ImageState
import com.example.front.screens.myshop.state.NewShopState
import com.example.front.screens.myshop.state.ShopIdState
import com.example.front.viewmodels.oneshop.OneShopViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class MyShopViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {


    private val _state = mutableStateOf(ShopIdState())
    var state: State<ShopIdState> = _state;

    private val _stateNewShop = mutableStateOf(NewShopState())
    var stateNewShop: State<NewShopState> = _stateNewShop;

    private val _stateimage = mutableStateOf(ImageState())
    var stateimage: State<ImageState> = _stateimage;

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

    fun newShop(shop: NewShopDTO, imagePart : MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.newShop(shop)

                if (response.isSuccessful) {
                    _stateNewShop.value = _stateNewShop.value.copy(
                        isLoading = false,
                        newShop = response.body(),
                        error = ""
                    )
                    stateNewShop.value!!.newShop!!.success?.let { uploadImage(2,it,imagePart) }
                }
                else{
                    _stateNewShop.value = _stateNewShop.value.copy(
                        isLoading = false,
                        newShop = null,
                        error = "CantAdd"
                    )
                }
            } catch (e: Exception) {
                _stateNewShop.value.error = e.message.toString()
            }
        }
    }

    fun uploadImage(type: Int, id: Int, imagePart : MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage2(type, id, imagePart)

                // Update the status based on the response
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

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}
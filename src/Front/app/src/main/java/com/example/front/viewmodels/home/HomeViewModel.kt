package com.example.front.viewmodels.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStoreManager
import com.example.front.model.CategoriesDTO
import com.example.front.model.ChosenCategoriesDTO
import com.example.front.repository.Repository
import com.example.front.screens.categories.CategoriesState
import com.example.front.screens.home.HomeProductsState
import com.example.front.screens.home.HomeShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(HomeProductsState())
    var state : State<HomeProductsState> = _state;

    private val _stateShop = mutableStateOf(HomeShopState())
    var stateShop : State<HomeShopState> = _stateShop;

    private var username = mutableStateOf("")
    val usernameState: State<String> = username

    fun getHomeProducts(id: Int)
    {
        viewModelScope.launch {
            try{
                val response = repository.getHomeProducts(id)
                Log.d("RESPONSE",response.toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        products = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun getHomeShops(id: Int)
    {
        viewModelScope.launch {
            try{
                val response = repository.getHomeShops(id)
                Log.d("RESPONSE",response.toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _stateShop.value = _stateShop.value.copy(
                        isLoading = false,
                        shops = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _stateShop.value.error = e.message.toString()
            }
        }
    }

    suspend fun getUsername() {
        val retrievedUsername = dataStoreManager.getUsernameFromToken()
        username.value = retrievedUsername ?: ""
        println(retrievedUsername)
    }

}
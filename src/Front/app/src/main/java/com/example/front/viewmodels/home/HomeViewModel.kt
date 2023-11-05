package com.example.front.viewmodels.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.CategoriesDTO
import com.example.front.model.ChosenCategoriesDTO
import com.example.front.repository.Repository
import com.example.front.screens.categories.CategoriesState
import com.example.front.screens.home.HomeProductsState
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _state = mutableStateOf(HomeProductsState())
    var state : State<HomeProductsState> = _state;
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
}
package com.example.front.viewmodels.shops

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.screens.sellers.states.ShopsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(ShopsState())
    var state: State<ShopsState> = _state;

    private val _stateFav = mutableStateOf(ShopsState())
    var stateFav: State<ShopsState> = _stateFav;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow


    fun getProducts(userId: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean) {
        viewModelScope.launch {
            try {
                val response = repository.getShops(userId,categories,rating,open,range,location,sort,search,page,favorite)
                Log.d("TAAAAG",response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(!favorite)
                    {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            shops = response.body()
                        )
                    }
                    else
                    {
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            shops = response.body()
                        )
                    }


                }
                else{
                    if(!favorite)
                    {
                        _state.value = _state.value.copy(
                            error = "Error"
                        )
                    }
                    else
                    {
                        _stateFav.value = _stateFav.value.copy(
                            error = "Error"
                        )
                    }
                }
            } catch (e: Exception) {
                if(!favorite)
                {
                    _state.value = _state.value.copy(
                        error = e.message.toString()
                    )
                }
                else{
                    _stateFav.value = _stateFav.value.copy(
                        error = e.message.toString()
                    )
                }
            }
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}

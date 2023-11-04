package com.example.front.viewmodels.categories

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.CategoriesDTO
import com.example.front.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.front.screens.categories.CategoriesState


class CategoriesViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<CategoriesDTO>>> = MutableLiveData()
    private val _state = mutableStateOf(CategoriesState())
    var state : State<CategoriesState> = _state;

    fun getCategoriesInfo()
    {
        viewModelScope.launch {
            try{
                val response = repository.getCategories()
                Log.d("RESPONSE",response.toString())
//                myResponse.value = response
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    Log.d("CATEGORIES:",responseBody.toString())
//                }
//                _state.value.isLoading = false
//                _state.value.categories = response.body()
//                state = _state
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("CATEGORIES:", responseBody.toString())

                    _state.value = _state.value.copy(
                        isLoading = false,
                        categories = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                Log.d("Categories error",e.message.toString())
                _state.value.error = e.message.toString()
            }
        }
    }
}


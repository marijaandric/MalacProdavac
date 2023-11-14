package com.example.front.viewmodels.categories

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.DTO.CategoriesDTO
import com.example.front.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.ChosenCategoriesDTO
import com.example.front.screens.categories.states.CategoriesState
import com.example.front.screens.categories.states.ChosenCategoriesState
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<CategoriesDTO>>> = MutableLiveData()
    private val _state = mutableStateOf(CategoriesState())
    var state : State<CategoriesState> = _state;

    private val _stateChosenCategories = mutableStateOf(ChosenCategoriesState())
    var stateChosenCategories : State<ChosenCategoriesState> = _stateChosenCategories;

    fun getCategoriesInfo()
    {
        viewModelScope.launch {
            try{
                val response = repository.getCategories()

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        categories = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _state.value.error = e.message.toString()
            }
        }
    }

    fun postCategories(categories: ChosenCategoriesDTO)
    {
        viewModelScope.launch {
            try{
                val response = repository.postCategories(categories)
                Log.d("RESPONSE",response.toString())

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    _stateChosenCategories.value = _stateChosenCategories.value.copy(
                        isLoading = false,
                        categoriesBool = response.body()
                    )
                }
            }
            catch (e:Exception)
            {
                _stateChosenCategories.value.error = e.message.toString()
            }
        }
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}


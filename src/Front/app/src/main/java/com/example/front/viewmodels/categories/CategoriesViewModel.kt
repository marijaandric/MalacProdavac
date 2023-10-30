package com.example.front.viewmodels.categories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.CategoriesDTO
import com.example.front.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoriesViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<CategoriesDTO>>> = MutableLiveData()

    fun getCategoriesInfo()
    {
        viewModelScope.launch {
            try{
                val response = repository.getCategories()
                Log.d("RESPONSE",response.toString())
                myResponse.value = response
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("CATEGORIES:",responseBody.toString())
                }
            }
            catch (e:Exception)
            {
                Log.d("Categories error",e.message.toString())
            }
        }
    }



}


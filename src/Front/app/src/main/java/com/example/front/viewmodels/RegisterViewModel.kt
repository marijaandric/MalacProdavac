package com.example.front.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.RegistrationRequest
import com.example.front.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class RegisterViewModel(private val repository: Repository) : ViewModel(){
    val myResponse: MutableLiveData<Response<Int>> = MutableLiveData()
    fun performRegistration(registrationRequest: RegistrationRequest) {
        viewModelScope.launch {
            try {
                val response = repository.register(registrationRequest)
                Log.d("RESPONSE",response.toString())
                myResponse.value = response
                if (response.isSuccessful) {
                    Log.d("MyApp", "IF")
                } else {
                    Log.d("MyApp", "ELSE")
                }
            } catch (e: Exception) {
                Log.d("MyApp", "CATCH")
            }
        }
    }
}
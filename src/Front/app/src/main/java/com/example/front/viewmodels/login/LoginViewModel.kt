package com.example.front.viewmodels.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.model.LoginDTO
import com.example.front.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class LoginViewModel(private val repository: Repository): ViewModel() {
    val myResponse:MutableLiveData<Response<Int>> = MutableLiveData()
    fun getLoginnInfo(login:LoginDTO)
    {
        viewModelScope.launch {
            try {
                val response = repository.getLogin(login)
                Log.d("RESPONSE",response.toString())
                myResponse.value = response
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("DOBIJEN ID:",responseBody.toString())
                }

            }
            catch (e:Exception)
            {
                Log.d("GRESKA",e.message.toString())
            }
        }
    }
}
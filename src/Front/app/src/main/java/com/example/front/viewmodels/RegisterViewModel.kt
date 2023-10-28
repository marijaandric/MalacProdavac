package com.example.front.viewmodels

import androidx.lifecycle.ViewModel
import com.example.front.models.RegistrationRequest

class RegisterViewModel : ViewModel(){

    fun performRegistration(
        name: String,
        lastName: String,
        email: String,
        password: String,
        address: String
    ) {
        val registrationRequest = RegistrationRequest(
            name,
            lastName,
            email,
            password,
            address,
            roleId = 1
        )


//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://localhost:7058/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)

//        viewModelScope.launch {
//            try {
//
//                val response = apiService.register(registrationRequest)
//
//                if (response.isExecuted) {
//                    Log.d("MyApp", "IF")
//                } else {
//                    Log.d("MyApp", "ELSE")
//                }
//            } catch (e: Exception) {
//                Log.d("MyApp", "CATCH")
//            }
//        }
    }
}

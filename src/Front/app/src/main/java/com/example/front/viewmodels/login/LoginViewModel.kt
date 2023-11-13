package com.example.front.viewmodels.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.LoginDTO
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    val jwtToken: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    fun getLoginInfo(login: LoginDTO) {
        viewModelScope.launch {
            try {
                val response = repository.getLogin(login)

                if (response.isSuccessful) {
                    val responseBody = response.body()!!.token
                    if (responseBody != null)
                        jwtToken.value = responseBody
                    else {
                        errorMessage.value = "Response body is null"
                    }
                } else {
                    if (response.code() == 400) {
                        val errorResponse = response.errorBody()?.string()
                        if (errorResponse != null) {
                            try {
                                val errorJson = JSONObject(errorResponse)
                                val errorMess = errorJson.optString("error")
                                if (errorMess.isNotEmpty()) {
                                    errorMessage.value = errorMess
                                } else {
                                    errorMessage.value = "Bad Request: An unknown error occurred 1"
                                }
                            } catch (e: JSONException) {
                                errorMessage.value = "Bad Request: An unknown error occurred 2"
                            }
                        } else {
                            errorMessage.value = "Bad Request: An unknown error occurred 3"
                        }
                    } else {
                        errorMessage.value = "Error: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    suspend fun performLogin(
        userInput: String,
        passwordInput: String
    ): Boolean {
        try {
            val data = LoginDTO(userInput, passwordInput)
            getLoginInfo(data)
            val token = jwtToken.value
            val errorMess = errorMessage.value
            if (token != null && errorMess == null) {
                dataStoreManager.storeToken(token)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    suspend fun setFirstTimeToFalse() {
        dataStoreManager.setFirstTime()
    }
}

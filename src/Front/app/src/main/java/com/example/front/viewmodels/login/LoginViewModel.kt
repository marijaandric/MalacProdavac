package com.example.front.viewmodels.login

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.MainActivity
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.LoginDTO
import com.example.front.repository.Repository
import com.example.front.screens.home.states.HomeProductsState
import com.example.front.screens.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val jwtToken: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    private val _state = mutableStateOf(LoginState())
    var state : State<LoginState> = _state;


    fun getLoginInfo(login: LoginDTO) : Job {
        return viewModelScope.launch {
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

            getLoginInfo(data).join()

            val token = jwtToken.value
            val errorMess = errorMessage.value
            if (token != null && errorMess == null) {
                dataStoreManager.storeToken(token)
                val intent = Intent(context.applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                _state.value = _state.value.copy(
                    loginState = true
                )
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _state.value = _state.value.copy(
            loginState = false,
            error = errorMessage.toString()
        )
        return false
    }

    suspend fun setFirstTimeToFalse() {
        dataStoreManager.setFirstTime()
    }
    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }
}

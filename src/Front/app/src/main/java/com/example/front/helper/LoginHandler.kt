package com.example.front.helper
import android.content.Context
import com.example.front.model.LoginDTO
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.repository.Repository
import javax.inject.Inject

class LoginHandler(private val context: Context) {
    private val repository = Repository()
    private val viewModel = LoginViewModel(repository)
    @Inject
    lateinit var dataStoreManager: DataStoreManager
    suspend fun performLogin(
        userInput: String,
        passwordInput: String
    ): Boolean {
        try {
            val data = LoginDTO(userInput, passwordInput)
            viewModel.getLoginInfo(data)
            val token = viewModel.jwtToken.value
            val errorMess = viewModel.errorMessage.value
            if (token != null && errorMess == null) {
                dataStoreManager.storeToken(token)
                return true
            }
        } catch (e: Exception) {
            // Handle the exception here (e.g., log or display an error message).
            e.printStackTrace()
        }
        return false
    }
}


package com.example.front.helper
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.front.model.LoginDTO
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.login.MainViewModelFactory
import com.example.front.helper.TokenManager
import com.example.front.navigation.Screen
import com.example.front.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class LoginHandler(private val context: Context) {
    private val repository = Repository()
    private val viewModel = LoginViewModel(repository)

    suspend fun performLogin(
        userInput: String,
        passwordInput: String,
    ): Boolean {
        val data = LoginDTO(userInput, passwordInput)
        viewModel.getLoginInfo(data)
        val token = viewModel.jwtToken.value
        val errorMess = viewModel.errorMessage.value

        if (token != null && errorMess == null) {
            val tokenManager = TokenManager(context)
            tokenManager.saveToken(token)
            return true
        }
        return false
    }
}


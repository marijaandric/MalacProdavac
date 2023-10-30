package com.example.front.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.front.screens.LoginScreen
import com.example.front.viewmodels.login.LoginViewModel

//Ovde idu sve komponente koje se prikazuju
@Composable
fun PostOfficeApp(viewModel: LoginViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LoginScreen(viewModel)
    }
}
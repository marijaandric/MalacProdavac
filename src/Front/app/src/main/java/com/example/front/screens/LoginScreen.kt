package com.example.front.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.HeaderImage
import com.example.front.components.LogoImage
import com.example.front.components.MyTextField
import com.example.front.components.TitleTextComponent
import com.example.front.model.LoginDTO
import com.example.front.viewmodels.login.LoginViewModel


@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // Define a mutable state for the user input
    var userInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderImage(
                painterResource(id = R.drawable.loginheaderimage),
            )

            // Logo Image
            LogoImage(
                painterResource(id = R.drawable.logowithwhitebackground),
                modifier = Modifier.offset(y = 75.dp)
            )

            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .align(Alignment.Center)
            ) {
                TitleTextComponent(value = stringResource(id = R.string.login_title))
                MyTextField(
                    labelValue = "Username",
                    painterResource = painterResource(id = R.drawable.user),
                    value = userInput, // Bind user input to the state
                    onValueChange = { userInput = it } // Update the state on value change
                )
                MyTextField(
                    labelValue = "Password",
                    painterResource = painterResource(id = R.drawable.padlock),
                    value = passwordInput, // Bind password input to the state
                    onValueChange = { passwordInput = it } // Update the state on value change
                )
                BigBlueButton(
                    text = "Login",
                    onClick = {
                        var data = LoginDTO(userInput, passwordInput)
                        viewModel.getLoginnInfo(data)
                        val response = viewModel.myResponse.value
                    },
                    width = 150f,
                    modifier = Modifier.offset(y = 150.dp)
                )
            }
        }
    }
}
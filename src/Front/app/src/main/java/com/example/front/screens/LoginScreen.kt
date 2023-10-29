package com.example.front.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.MediumBlueButton
import com.example.front.R
import com.example.front.components.HeaderImage
import com.example.front.components.LogoImage
import com.example.front.components.MyTextField
import com.example.front.components.TitleTextComponent

@Composable
fun LoginScreen() {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Image
            HeaderImage(
                painterResource(id = R.drawable.loginheaderimage),
            )

            // Logo Image
            LogoImage(
                painterResource(id = R.drawable.logowithwhitebackground),
                modifier = Modifier.offset(y = 75.dp)
            )

            // Login Form
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .align(Alignment.Center)
            ) {
                TitleTextComponent(value = stringResource(id = R.string.login_title))
                MyTextField(
                    labelValue = "Username",
                    painterResource = painterResource(id = R.drawable.user)
                )
                MyTextField(
                    labelValue = "Password",
                    painterResource = painterResource(id = R.drawable.padlock))
                MediumBlueButton("Login",{},120f)
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){
    LoginScreen()
}
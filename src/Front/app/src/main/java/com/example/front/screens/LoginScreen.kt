package com.example.front.screens

import android.annotation.SuppressLint
import android.media.session.MediaSession.Token
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
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.HeaderImage
import com.example.front.components.LogoImage
import com.example.front.components.MyTextField
import com.example.front.components.TitleTextComponent
import com.example.front.navigation.Screen
import com.example.front.repository.Repository
import com.example.front.viewmodels.login.LoginViewModel
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.front.helper.DataStoreManager
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController:NavHostController,
    dataStoreManager: DataStoreManager
) {
    var userInput by remember { mutableStateOf("marija.andric") }
    var passwordInput by remember { mutableStateOf("MejoSmrdi123!") }
    var errorMessage by remember { mutableStateOf("") }

    lateinit var viewModel: LoginViewModel
    val repository = Repository() // Create a Repository instance
    viewModel = LoginViewModel(repository,dataStoreManager)

    var scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(scaffoldState = scaffoldState, content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HeaderImage(
                    painterResource(id = R.drawable.loginheaderimage),
                )

                // Logo Image
                LogoImage(
                    painterResource(id = R.drawable.logowithwhitebackground),
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(20.dp)
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
                        onValueChange = { passwordInput = it }, // Update the state on value change
                        isPassword = true
                    )
                    BigBlueButton(
                        text = "Login",
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val success = viewModel.performLogin(userInput, passwordInput)
                                    if (success) {
                                        navController.navigate(route = Screen.Home.route)
                                    } else {
                                        val errorMess = viewModel.errorMessage.value
                                        if (errorMess != null) {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = errorMess.toString(),
                                                actionLabel = "Try again",
                                                duration = SnackbarDuration.Indefinite
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Handle the exception here (e.g., log or display an error message).
                                    e.printStackTrace()
                                }
                            }
                        },
                        width = 150f,
                        modifier = Modifier.offset(y = 100.dp)
                    )
                }
            }
        })
    }
}


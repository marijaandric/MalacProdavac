package com.example.front.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import android.media.session.MediaSession.Token
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.front.viewmodels.login.LoginViewModel
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.front.viewmodels.categories.CategoriesViewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    var userInput by remember { mutableStateOf("milica.vasovic") }
    var passwordInput by remember { mutableStateOf("ComeSmrdi123!") }

    var scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.setFirstTimeToFalse()
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(scaffoldState = scaffoldState, content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(

                )
                {
                    HeaderImage(
                        painterResource(id = R.drawable.loginheaderimage),
                    )

                    // Logo Image
                    Box(
                        modifier = Modifier.align(Alignment.BottomCenter)
                            .padding(top=30.dp)
                    )
                    {
                        LogoImage(
                            painterResource(id = R.drawable.logowithwhitebackground),
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(20.dp)
                ) {
                    TitleTextComponent(value = stringResource(id = R.string.login_title))
                    MyTextField(
                        labelValue = "Username",
                        painterResource = painterResource(id = R.drawable.user),
                        value = userInput,
                        onValueChange = { userInput = it }
                    )
                    MyTextField(
                        labelValue = "Password",
                        painterResource = painterResource(id = R.drawable.padlock),
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        isPassword = true,
                        isLast = true
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                    BigBlueButton(
                        text = "Login",
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val success = viewModel.performLogin(userInput, passwordInput)
                                    if (success) {
//                                        navController.navigate(route = Screen.Home.route) {
//                                            popUpTo("auth") { inclusive = true }
//                                        }
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
                                    e.printStackTrace()
                                }
                            }
                        },
                        width = 150f,
                        modifier = Modifier
                    )
                    Text(
                        text = "Don't have an account? Register now.",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.Register.route)
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF005F8B),
                        fontWeight = FontWeight(600),
                        fontSize = 16.sp
                    )
                    if(viewModel.state.value.loginState == true)
                    {

                    }
                }
            }
        })
    }
}


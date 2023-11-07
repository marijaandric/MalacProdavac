package com.example.front.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ErrorTextComponent
import com.example.front.components.HeaderImage
import com.example.front.components.LogoImage
import com.example.front.components.MyTextField
import com.example.front.components.TitleTextComponent
import com.example.front.helper.DataStoreManager
import com.example.front.model.RegistrationRequest
import com.example.front.navigation.Screen
import com.example.front.repository.Repository
import com.example.front.viewmodels.register.RegisterViewModel

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
fun isValidPassword(password: String): String {
    val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-\\.]).{8,}$")
    if (regex.matches(password))
        return ""
    return "Password must have at least 8 characters and contain at least one of the following: upper case letter, lower case letter, number, symbol"
}

@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel) {

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // greske pri unosu
    var nameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            HeaderImage(painterResource(id = R.drawable.loginheaderimage))
            LogoImage(
                painterResource(id = R.drawable.logowithwhitebackground),
                modifier = Modifier.offset(y = (-16).dp)
            )

            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .padding(top = 70.dp)
                    .align(Alignment.Center)
            ){
                TitleTextComponent("Registracija")

                MyTextField(
                    labelValue = "First name",
                    painterResource = painterResource(id = R.drawable.user),
                    value = name,
                    onValueChange = { name = it }
                )
                if(nameError.isNotEmpty()) {
                    ErrorTextComponent(nameError)
                }

                MyTextField(
                    labelValue = "Last name",
                    painterResource = painterResource(id = R.drawable.user),
                    value = lastName,
                    onValueChange = { lastName = it }
                )
                if(lastNameError.isNotEmpty()) {
                    ErrorTextComponent(lastNameError)
                }

                MyTextField(
                    labelValue = "Email",
                    painterResource = painterResource(id = R.drawable.mail),
                    value = email,
                    onValueChange = { email = it }
                )
                if(emailError.isNotEmpty()) {
                    ErrorTextComponent(emailError)
                }

                MyTextField(
                    labelValue = "Password",
                    painterResource = painterResource(id = R.drawable.padlock),
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true
                )
                if(passwordError.isNotEmpty()) {
                    ErrorTextComponent(passwordError)
                }

                MyTextField(
                    labelValue = "Confirm password",
                    painterResource = painterResource(id = R.drawable.padlock),
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it },
                    isPassword = true
                )
                if(confirmPasswordError.isNotEmpty()) {
                    ErrorTextComponent(confirmPasswordError)
                }

                MyTextField(
                    labelValue = "Address",
                    painterResource = painterResource(id = R.drawable.home),
                    value = address,
                    onValueChange = { address = it }
                )
                if(addressError.isNotEmpty()) {
                    ErrorTextComponent(addressError)
                }

                Button(
                    onClick = {
                        nameError = ""
                        lastNameError = ""
                        emailError = ""
                        passwordError = ""
                        confirmPasswordError = ""
                        addressError = ""

                        if (name.isEmpty()) {
                            nameError = "First name is required"
                        }
                        if (lastName.isEmpty()) {
                            lastNameError = "Last name is required"
                        }
                        if (!isValidEmail(email)) {
                            emailError = "Email is not valid"
                        }
                        passwordError = isValidPassword(password)
                        if(password != passwordConfirm) {
                            confirmPasswordError = "Passwords don't match"
                        }
                        if (address.isEmpty()) {
                            addressError = "Address is required"
                        }

                        if (nameError.isEmpty() &&
                            lastNameError.isEmpty() &&
                            emailError.isEmpty() &&
                            passwordError.isEmpty() &&
                            confirmPasswordError.isEmpty() &&
                            addressError.isEmpty()
                            ) {
                            val data = RegistrationRequest(name, lastName, email, password, address, 1)
                            registerViewModel.performRegistration(data)
                            val response = registerViewModel.myResponse.value
                            Log.e("RESPONSE", response.toString())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 53.dp)
                        .padding(top = 14.dp, bottom = 14.dp),
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = Color
                                .hsl(205f, 0.44f,0.63f)
                        ),
                ) {
                    Text(
                        text = "Registruj se",
                        modifier = Modifier
                            .height(25.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.lexend)),
                            fontWeight = FontWeight(300),
                            textAlign = TextAlign.Center,
                        )
                    )
                }

                Text(
                    text = "Already have an account? Login.",
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Categories.route)
                    }
                )
            }

        }
    }
}
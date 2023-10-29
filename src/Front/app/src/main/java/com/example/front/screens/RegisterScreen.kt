package com.example.front.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.front.R
import com.example.front.components.HeaderImage
import com.example.front.components.TitleTextComponent
import com.example.front.viewmodels.RegisterViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    val registerViewModel: RegisterViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

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

            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .align(Alignment.Center)
            ){
                TitleTextComponent("Registracija")

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.hsl(0f, 0f, 0.95f))
                    //            label = { Text("Ime") }
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Prezime") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Šifra") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        registerViewModel.performRegistration(name, lastName, email, password, address)
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
            }

        }
    }
}

@Preview
@Composable
fun prikaziRegister(){
    RegisterScreen()
}
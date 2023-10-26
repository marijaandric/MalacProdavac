package com.example.front

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.SolidColor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

var apiResponse = ""
fun sendDataToApi(loginData: LoginData) {
    val client = OkHttpClient()

    // Define the request URL (replace with your API endpoint)
    val url = "http://localhost:8080"

    // Convert the `LoginData` object to a JSON string
    val json = """
        {
            "username": "${loginData.username}",
            "password": "${loginData.password}"
        }
    """.trimIndent()

    val mediaType = "application/json".toMediaTypeOrNull()
    val requestBody = json.toRequestBody(mediaType)

    // Create the POST request
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    // Execute the request
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val responseData = response.body?.string()
        // Handle the successful response data here
        apiResponse = "Response data: $responseData"
    } else {
        // Handle the failure here
        apiResponse = "Request failed: ${response.code} - ${response.message}"
    }
}


@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginForm(viewModel)
        Text(text = viewModel.apiResponse)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username input in a separate row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            InputField(
                modifier = Modifier.weight(1f),
                placeholderText = "Username",
                text = usernameState.value,
                onTextChanged = { usernameState.value = it }
            )
        }

        // Password input in a separate row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            InputField(
                modifier = Modifier.weight(1f),
                placeholderText = "Password",
                text = passwordState.value,
                onTextChanged = { passwordState.value = it }
            )
        }

        // Log in button in a separate row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            BlueButton(
                text = "Log in",
                onClick = {
                    val loginData = LoginData(usernameState.value, passwordState.value)
                    viewModel.sendDataToApi(loginData)
                },
                modifier = Modifier.width(300.dp)
            )
        }
    }
}



@Composable
fun HeaderImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.loginheaderimage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun LogoImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logowithwhitebackground),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.then(
            Modifier.scale(0.7f)
            )
    )
}

@Composable
fun TitleText(text: String, modifier: Modifier = Modifier) {

    val customFontFamily = FontFamily(Font(R.font.lexend))

    Text(
        text = text,
        modifier = modifier,
        color = Color.Black, // Text color,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            fontFamily = customFontFamily,
            fontSize = 40.sp,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    text: String,
    onTextChanged: (String) -> Unit
) {
    val customFontFamily = FontFamily(Font(R.font.lexend))

    Box(
        modifier = modifier.then(
            Modifier.border(
                width = 2.dp,
                color = Color(0xFF77A7CA),
                shape = RoundedCornerShape(20.dp)
            )
                .width(300.dp)
                .background(Color.Gray) // Gray background color
        )
    ) {
        BasicTextField(
            value = text,
            onValueChange = { onTextChanged(it) }, // Pass text changes to the callback
            textStyle = TextStyle(
                fontFamily = customFontFamily,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
        )

        // Add the placeholder text when the text is empty
        if (text.isEmpty()) {
            Text(
                text = placeholderText,
                style = TextStyle(
                    fontFamily = customFontFamily,
                    fontSize = 16.sp,
                    color = Color.Gray
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
            )
        }
    }
}





@Composable
fun BlueButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customFontFamily = FontFamily(Font(R.font.lexend)) // Your custom font family

    Button(
        onClick = onClick,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(Color(0xFF77A7CA))
    ) {
        Text(
            text = text,
            color = Color.White,
            style = TextStyle(
                fontFamily = customFontFamily, // Set the custom font family
                fontSize = 20.sp
            )
        )
    }
}


package com.example.front

import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class LoginViewModel : ViewModel() {
    var apiResponse = "" // This should be encapsulated with LiveData for a more robust solution

    fun sendDataToApi(loginData: LoginData) {
        // Move your API request code here, and update apiResponse accordingly
        val client = OkHttpClient()
        val url = "http://localhost:8080" // Replace with your API endpoint

        val json = """
            {
                "username": "${loginData.username}",
                "password": "${loginData.password}"
            }
        """.trimIndent()

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseData = response.body?.string()
            apiResponse = "Response data: $responseData"
        } else {
            apiResponse = "Request failed: ${response.code} - ${response.message}"
        }
    }
}

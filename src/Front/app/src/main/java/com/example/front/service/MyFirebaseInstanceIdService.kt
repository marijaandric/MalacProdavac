package com.example.front.service

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FCMTokenManager {

    private const val FCM_TOKEN_PREF = "FCM_TOKEN_PREF"

    fun getFCMToken(context: Context) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fcmToken = task.result
                    Log.d("FCM Token", "Token: $fcmToken")

                    // Now you can use or store the FCM token as needed.
                    // For example, you might want to send it to your server.
                    sendTokenToServer(fcmToken)
                } else {
                    // Handle the error.
                    Log.e("FCM Token", "Error getting FCM token", task.exception)
                }
            }
    }

    // Method to send the FCM token to your server.
    private fun sendTokenToServer(token: String?) {
        // Implement your logic to send the FCM token to your server.
        // This might involve making a network request to your backend.
        // You can customize this method based on your server communication.
        Log.d("FCM Token", "Sending token to server: $token")

        // Example: Store the FCM token in your database for the current user.
        //storeTokenInDatabase(token)
    }
}


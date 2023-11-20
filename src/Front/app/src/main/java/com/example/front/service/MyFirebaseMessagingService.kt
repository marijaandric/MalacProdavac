package com.example.front.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.front.R
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.random
import javax.inject.Inject

//        CoroutineScope(Dispatchers.IO).launch {
//            dataStoreManager.getUserIdFromToken()?.let { repository.saveFCMToken(it, token) }
//        }
@AndroidEntryPoint
class MyFirebaseMessagingService @Inject constructor() : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TOKEN MOJ TOKEN TOKEN MOJ",token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
            Log.d("PORUKA","${it.body} + ${it.body}")
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                R.string.default_notification_channel_id.toString(),
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, R.string.default_notification_channel_id.toString())
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_stat_name)

        notificationManager.notify(random().toInt(), notificationBuilder.build())
    }
}
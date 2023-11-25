package com.example.front

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults.colors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.front.components.DrawerItems
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.navigation.SetupNavGraph
import com.example.front.ui.theme.FrontTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    lateinit var navController: NavHostController

    @Inject
    lateinit var dataStoreManager: DataStoreManager


    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    companion object {

        private const val TAG = "MainActivity"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW,
                ),
            )
        }

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.getString(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

        askNotificationPermission()

        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val logged = runBlocking {
                dataStoreManager.isLoggedIn()
            }

            FrontTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    navController = rememberNavController()

                    val items = DrawerItems

                    if (logged) {
                        val roleId = runBlocking { dataStoreManager.getRoleId() }
                        val filteredItems = items.filter { it.roleId == roleId || it.roleId == 1 }
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    modifier = Modifier
                                        .width(324.dp),
                                    drawerContainerColor = Color(0xFF294E68)
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .height(56.dp)
                                    )

                                    filteredItems.forEach { item ->
                                        NavigationDrawerItem(
                                            label = { Text(text = item.label) },
                                            selected = false,
                                            onClick = {
                                                scope.launch { drawerState.close() }
                                                navController.navigate(route = item.route)
                                            },
                                            icon = {
                                                Icon(
                                                    painter = painterResource(id = item.icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.height(24.dp)
                                                )
                                            },
                                            colors = colors(
                                                unselectedContainerColor = Color(0xFF294E68),
                                                selectedContainerColor = Color(0xFF263e52),
                                                unselectedTextColor = Color.White,
                                                selectedTextColor = Color.White,
                                                unselectedIconColor = Color.White,
                                                selectedIconColor = Color.White,
                                            ),
                                            shape = RectangleShape
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 12.dp, bottom = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    )
                                    {
                                        Image(
                                            painter = painterResource(id = R.drawable.logout),
                                            contentDescription = null,
                                            modifier = Modifier.height(24.dp)
                                        )
                                        Text(
                                            text = "Logout",
                                            modifier = Modifier
                                                .clickable {
                                                    //pokaze modal da potvrdi da zeli da se izloguje
                                                    //ukloni token
                                                    //vodi na Login
                                                    runBlocking {
                                                        dataStoreManager.storeToken("")
                                                    }
                                                    val intent = Intent(
                                                        this@MainActivity,
                                                        MainActivity::class.java
                                                    )
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                },
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            },
                            content = {
                                SetupNavGraph(navController = navController)
                            }
                        )
                    } else {
                        SetupNavGraph(navController = navController)
                    }

                }
            }
        }
    }
}
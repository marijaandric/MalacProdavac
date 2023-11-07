    package com.example.front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.front.helper.DataStoreManager
import com.example.front.navigation.SetupNavGraph
import com.example.front.screens.ProductPage
import com.example.front.screens.SplashScreen
import com.example.front.screens.categories.RegistrationCategories
import com.example.front.screens.home.HomePage
import com.example.front.ui.theme.FrontTheme
import com.example.front.viewmodels.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

    @AndroidEntryPoint
    class MainActivity : ComponentActivity() {
        lateinit var navController: NavHostController
        @Inject
        lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)) {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController, dataStoreManager)
                }
            }
        }
    }
}
package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.front.screens.HomePage
import com.example.front.screens.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
        ){
        composable(
            route = Screen.SplashScreen.route
        )
        {
            SplashScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route
        ){
            HomePage()
        }
        introNavGraph(navController = navController)
        authNavGraph(navController = navController)
    }
}
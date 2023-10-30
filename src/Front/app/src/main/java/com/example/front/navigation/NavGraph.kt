package com.example.front.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.front.screens.HomePage
import com.example.front.screens.LoginScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
        ){
        composable(
            route = Screen.Login.route
        ){
            LoginScreen(navController = rememberNavController())
        }
        composable(
            route = Screen.Home.route
        ){
            HomePage()
        }
    }
}
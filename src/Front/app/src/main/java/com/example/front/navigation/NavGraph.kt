package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.front.helper.DataStoreManager
import com.example.front.screens.home.HomePage
import com.example.front.screens.Intro
import com.example.front.screens.LoginScreen
import com.example.front.screens.SplashScreen
import com.example.front.screens.categories.RegistrationCategories
import com.example.front.viewmodels.home.HomeViewModel
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.register.RegisterViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetupNavGraph(
    navController: NavHostController
){

    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    //val registerViewModel: RegisterViewModel = hiltViewModel()
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
            route = Screen.Login.route
        ){
            LoginScreen(navController = navController,loginViewModel)
        }
        composable(
            route = Screen.Home.route
        ){
            HomePage(navController = navController, homeViewModel)
        }
        composable(
            route=Screen.Categories.route
        )
        {
            RegistrationCategories(navController = navController)
        }
        introNavGraph(navController = navController)
        authNavGraph(navController = navController, loginViewModel = loginViewModel, registerViewModel = registerViewModel)
    }
}
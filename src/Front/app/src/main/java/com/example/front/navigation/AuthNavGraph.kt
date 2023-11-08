package com.example.front.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.front.screens.login.LoginScreen
import com.example.front.screens.register.RegisterScreen
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.register.RegisterViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    navigation(
        startDestination = Screen.Login.route,
        route = "auth"
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController, registerViewModel = registerViewModel)
        }
    }
}
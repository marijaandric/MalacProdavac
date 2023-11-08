package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.front.screens.home.HomePage
import com.example.front.screens.splas_and_intro.Intro
import com.example.front.screens.login.LoginScreen
import com.example.front.screens.splas_and_intro.SplashScreen
import com.example.front.screens.categories.RegistrationCategories
import com.example.front.viewmodels.categories.CategoriesViewModel
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
    val categoriesViewModel : CategoriesViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "intro"
        ){
        composable(
            route = Screen.Home.route
        ){
            HomePage(navController = navController, homeViewModel)
        }
        composable(
            route=Screen.Categories.route
        )
        {
            RegistrationCategories(navController = navController, categoriesViewModel)
        }
        introNavGraph(navController = navController)
        authNavGraph(navController = navController, loginViewModel = loginViewModel, registerViewModel = registerViewModel)
    }
}
package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.front.components.Sidebar
import com.example.front.screens.home.HomePage
import com.example.front.screens.categories.RegistrationCategories
import com.example.front.screens.product.ProductPage
import com.example.front.screens.userprofile.UserProfileScreen
import com.example.front.viewmodels.splasintro.SplashAndIntroViewModel
import com.example.front.viewmodels.categories.CategoriesViewModel
import com.example.front.viewmodels.home.HomeViewModel
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.myprofile.MyProfileViewModel
import com.example.front.viewmodels.product.ProductViewModel
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
    val productViewModel: ProductViewModel = hiltViewModel()
    val splashViewModel: SplashAndIntroViewModel = hiltViewModel()
    val myProfileViewModel : MyProfileViewModel = hiltViewModel()
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
        composable(
            route = Screen.Product.route
        ){
            ProductPage(navController, productViewModel)
        }
        composable(
            route = Screen.MyProfile.route
        )
        {
            UserProfileScreen(navController = navController, myProfileViewModel)
        }

        introNavGraph(navController = navController, splashViewModel)
        authNavGraph(navController = navController, loginViewModel = loginViewModel, registerViewModel = registerViewModel, categoriesViewModel)
    }
}
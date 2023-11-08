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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController
){

    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val categoriesViewModel : CategoriesViewModel = hiltViewModel()
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
            RegistrationCategories(navController = navController, categoriesViewModel)
        }






        composable(route=Screen.Intro1.route)
        {
            Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
            val desc = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            val pagerState = rememberPagerState(
                initialPageOffsetFraction = 0f
            )
            Scaffold() {
                HorizontalPager(pageCount = 4, state = pagerState) { page ->
                    val pageContent = when (page) {
                        0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                        1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                        2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                        3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                        else -> error("Invalid page: $page")
                    }
                    pageContent
                }
            }
        }
        composable(route=Screen.Intro2.route)
        {
            val desc = "Showcase your products and engage with local customers! Increase your visibility and sales."
            Intro("Business owners",desc,"intro2",1,navController = navController)
            val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            val pagerState = rememberPagerState(
                initialPage = 1,
                initialPageOffsetFraction = 0f
            )

            Scaffold() {
                HorizontalPager(pageCount = 4,state = pagerState) { page->
                    val pageContent = when (page) {
                        0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                        1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                        2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                        3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                        else -> error("Invalid page: $page")
                    }
                    pageContent
                }
            }
        }
        composable(route = Screen.Intro3.route)
        {
            val desc = "Explore a wide range of local products and services. Support your community."
            Intro("Customers",desc,"intro3",2,navController = navController)
            val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            val pagerState = rememberPagerState(
                initialPage = 2,
                initialPageOffsetFraction = 0f
            )
            Scaffold() {
                HorizontalPager(pageCount = 4,state = pagerState) { page->
                    val pageContent = when (page) {
                        0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                        1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                        2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                        3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                        else -> error("Invalid page: $page")
                    }
                    pageContent
                }
            }
        }
        composable(route = Screen.Intro4.route)
        {
            val desc = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            Intro("Delivery people",desc,"intro4",3,navController = navController)
            val pagerState = rememberPagerState(
                initialPage = 3,
                initialPageOffsetFraction = 0f
            )
            val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            Scaffold() {
                HorizontalPager(pageCount = 4,state = pagerState) { page->
                    val pageContent = when (page) {
                        0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                        1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                        2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                        3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                        else -> error("Invalid page: $page")
                    }
                    pageContent
                }
            }
        }
    }
}
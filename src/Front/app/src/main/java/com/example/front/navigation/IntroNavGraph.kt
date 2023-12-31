package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.front.screens.splas_and_intro.Intro
import com.example.front.screens.splas_and_intro.SplashScreen
import com.example.front.viewmodels.splasintro.SplashAndIntroViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.introNavGraph(
    navController: NavHostController,
    splashViewModel: SplashAndIntroViewModel
) {
    navigation(
        startDestination = Screen.SplashScreen.route,
        route = "intro"
    ) {
        composable(
            route = Screen.SplashScreen.route
        )
        {
            SplashScreen(navController = navController, splashViewModel)
        }
        composable(route=Screen.Intro1.route)
        {
            Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
            val desc = "Showcase your products and engage with local customers! Increase your visibility and sales."
            val desc2 = "Explore a wide range of local products and services. Support your community."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            val pagerState = rememberPagerState(
                initialPageOffsetFraction = 0f,
                pageCount = { 4 }
            )
            Scaffold() {
                HorizontalPager(state = pagerState) { page ->
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
            val desc2 = "Explore a wide range of local products and services. Support your community."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            val pagerState = rememberPagerState(
                initialPage = 1,
                initialPageOffsetFraction = 0f,
                pageCount = { 4 }
            )

            Scaffold() {
                HorizontalPager(state = pagerState) { page->
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
            val desc = "Showcase your products and engage with local customers. Increase your visibility and sales."
            val desc2 = "Explore a wide range of local products and services. Support your community."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            Intro("Customers",desc2,"intro3",2,navController = navController)
            val pagerState = rememberPagerState(
                initialPage = 2,
                initialPageOffsetFraction = 0f,
                pageCount = { 4 }
            )
            Scaffold() {
                HorizontalPager(state = pagerState) { page->
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
            val desc = "Showcase your products and engage with local customers. Increase your visibility and sales."
            val desc2 = "Explore a wide range of local products and services. Support your community."
            val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
            Intro("Delivery people",desc3,"intro4",3,navController = navController)
            val pagerState = rememberPagerState(
                initialPage = 3,
                initialPageOffsetFraction = 0f,
                pageCount = { 4 }
            )
            Scaffold() {
                HorizontalPager(state = pagerState) { page->
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
package com.example.front.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.front.screens.products.AllProducts
import com.example.front.screens.home.HomePage
import com.example.front.screens.categories.RegistrationCategories
import com.example.front.screens.product.ProductPage
import com.example.front.screens.sellers.SellersScreen
import com.example.front.screens.shop.ShopScreen
import com.example.front.screens.userprofile.UserProfileScreen
import com.example.front.viewmodels.splasintro.SplashAndIntroViewModel
import com.example.front.viewmodels.categories.CategoriesViewModel
import com.example.front.viewmodels.home.HomeViewModel
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.myprofile.MyProfileViewModel
import com.example.front.viewmodels.oneshop.OneShopViewModel
import com.example.front.viewmodels.product.ProductViewModel
import com.example.front.viewmodels.register.RegisterViewModel
import com.example.front.viewmodels.shops.ShopsViewModel

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
    val shopsViewModel: ShopsViewModel = hiltViewModel()
    val oneShopViewModel : OneShopViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "intro"
        //startDestination = "sellers"
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
            route = "${Screen.Product.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)
            val productId = arguments.getInt("id")
            ProductPage(navController, productViewModel, productId)
        }

        composable(
            route = Screen.MyProfile.route
        )
        {
            UserProfileScreen(navController = navController, myProfileViewModel)
        }

        composable(
            route = Screen.AllProduct.route
        )
        {
            AllProducts(navController = navController, homeViewModel)
        }
        composable(
            route = Screen.AllSellers.route
        )
        {
            SellersScreen(navController = navController,shopsViewModel)
        }
        composable(
            route = "${Screen.Shop.route}/{id}",
            arguments = listOf(navArgument("id") { type= NavType.IntType})
        )
        {navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)
            val productId = arguments.getInt("id")
            ShopScreen(navController = navController,oneShopViewModel, productId)
        }


        introNavGraph(navController = navController, splashViewModel)
        authNavGraph(navController = navController, loginViewModel = loginViewModel, registerViewModel = registerViewModel, categoriesViewModel)
    }
}
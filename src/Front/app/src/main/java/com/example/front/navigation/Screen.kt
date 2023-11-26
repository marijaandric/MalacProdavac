package com.example.front.navigation

sealed class Screen(val route:String){
    object Login:Screen(route = "login_screen")
    object Register:Screen(route = "register_screen")
    object Home:Screen(route = "home_screen")
    object SplashScreen: Screen(route = "splash_screen")
    object Intro1:Screen(route = "intro1")
    object Intro2:Screen(route="intro2")
    object Intro3:Screen(route="intro3")
    object Intro4:Screen(route="intro4")
    object Categories:Screen(route = "choose_categories")
    object Product:Screen(route = "product")
    object MyProfile:Screen(route="my_profile")
    object AllProduct:Screen(route="products")
    object AllSellers:Screen(route="sellers")
    object Shop:Screen(route="shop")
    object MyShop:Screen(route="my_shop")
    object SetUpShop:Screen(route="setup_shop")
}

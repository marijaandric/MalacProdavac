package com.example.front.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.front.screens.cart.Cart
import com.example.front.screens.checkout.CheckoutScreen
import com.example.front.viewmodels.cart.CartViewModel
import com.example.front.viewmodels.checkout.CheckoutViewModel

fun NavGraphBuilder.orderingNavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
) {
    navigation(
        startDestination = Screen.Cart.route,
        route = "ordering"
    ) {
        composable(route = Screen.Cart.route) {
            Cart(viewModel = cartViewModel, navController = navController)
        }
        composable(route = Screen.Checkout.route) {
            CheckoutScreen(viewModel = checkoutViewModel, navController = navController)
        }
    }
}
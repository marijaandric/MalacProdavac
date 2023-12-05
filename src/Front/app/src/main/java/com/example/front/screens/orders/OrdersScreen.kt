package com.example.front.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.front.components.FourTabs
import com.example.front.components.OrderCard
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle

@Composable
fun OrdersScreen(navController: NavHostController,) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedTabIndex by remember{
        mutableStateOf(0)
    }
//    Sidebar(
//        drawerState,
//        navController,
//        shopViewModel.dataStoreManager
//    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    )
    {
        item{
            SmallElipseAndTitle(title = "Orders", drawerState = drawerState)
        }
        item {
            FourTabs(
                onFirstTabSelected = { selectedTabIndex = 0 },
                onSecondTabSelected = { selectedTabIndex = 1 },
                onThirdTabSelected = { selectedTabIndex = 2 },
                onFourthTabSelected = { selectedTabIndex = 3},
                selectedColumnIndex = selectedTabIndex,
                firstTab = "All",
                secondTab = "Delivered",
                thirdTab = "Pending",
                fourthTab = "Ready for pickup",
                isFilters = true
            )
        }
        item{
            Orders()
        }
    }
}

@Composable
fun Orders() {
    OrderCard(orderid = "Order No1037088", quantity = 2, amount = 300.00f, date = "20.1.2023", status="Delivered")
}


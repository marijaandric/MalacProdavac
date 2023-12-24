package com.example.front.screens.orders

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.FourTabs
import com.example.front.components.OrderCard
import com.example.front.components.Paginator
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.viewmodels.orders.OrdersViewModel

@Composable
fun OrdersScreen(navController: NavHostController,ordersViewModel: OrdersViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedTabIndex by remember{
        mutableStateOf(0)
    }
    var currentPage by remember{
        mutableStateOf(1)
    }
    var userId by remember{
        mutableStateOf(0)
    }
    LaunchedEffect(Unit)
    {
        ordersViewModel.getUserId()?.let { ordersViewModel.getOrders(it, null, null) }
        ordersViewModel.getUserId()?.let{ ordersViewModel.getOrdersPage(it, null); userId = it}
    }
    LaunchedEffect(selectedTabIndex) {
        if(selectedTabIndex == 0)
        {
            ordersViewModel.getUserId()?.let { ordersViewModel.getOrders(it, null, currentPage) }
            ordersViewModel.getUserId()?.let{ ordersViewModel.getOrdersPage(it, null);}
        }
        else{
            ordersViewModel.getUserId()?.let { ordersViewModel.getOrders(it, selectedTabIndex, currentPage) }
            ordersViewModel.getUserId()?.let{ ordersViewModel.getOrdersPage(it, selectedTabIndex);}
        }
    }
    Sidebar(
        drawerState,
        navController,
        ordersViewModel.dataStoreManager
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        )
        {
            item{
                SmallElipseAndTitle(title = "My orders", drawerState = drawerState)
            }
            item {
                FourTabs(
                    onFirstTabSelected = { selectedTabIndex = 0 },
                    onSecondTabSelected = { selectedTabIndex = 2 },
                    onThirdTabSelected = { selectedTabIndex = 4 },
                    onFourthTabSelected = { selectedTabIndex = 1},
                    selectedColumnIndex = selectedTabIndex,
                    firstTab = "All",
                    secondTab = "Pending",
                    thirdTab = "Processing",
                    fourthTab = "Delivered",
                    isFilters = true
                )
            }
            item{
                if(ordersViewModel.state.value.isLoading)
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 170.dp),
                        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else if(ordersViewModel.state.value.error.contains("NotFound"))
                {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 170.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.nofound), contentDescription = null, modifier = Modifier.size(200.dp))
                        Text("No orders found", style = MaterialTheme.typography.titleSmall)
                    }
                }
                else{
                    Orders(ordersViewModel, navController)
                }
            }
            item {
                val totalPages = ordersViewModel.statePageCount.value
                if(!totalPages.isLoading)
                {
                    if(totalPages.ordersPage!!.count != 0 )
                    {
                        Paginator(currentPage = currentPage, totalPages = totalPages.ordersPage!!.count, onPageSelected = { newPage ->
                            if(newPage in 1..totalPages.ordersPage!!.count)
                            {
                                currentPage = newPage
                                ordersViewModel.getOrders(userId, selectedTabIndex, currentPage)
                            }
                        })
                    }
                }
//                    totalPages = shopsViewModel.statePageCount.value
//            Paginator(
//                currentPage = currentPage,
//                totalPages = totalPages,
//                onPageSelected = { newPage ->
//                    if (newPage in 1..totalPages) {
//                        currentPage = newPage
//                        shopsViewModel.ChangePage(currentPage)
//                    }
//                }
//            )
            }
        }
    }

}

@Composable
fun Orders(ordersViewModel: OrdersViewModel, navController: NavHostController) {

    var orders = ordersViewModel.state.value.orders
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        orders.forEach { order ->
            OrderCard(
                orderid = "Order No${order.id}",
                quantity = order.quantity,
                amount = order.amount,
                date = order.createdOn,
                status = order.status,
                navController = navController,
                order.id
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    //OrderCard(orderid = "Order No1037088", quantity = 2, amount = 300.00f, date = "20.1.2023", status="Delivered")
}


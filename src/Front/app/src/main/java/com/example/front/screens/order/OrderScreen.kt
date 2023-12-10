package com.example.front.screens.order

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.CardButton
import com.example.front.components.CardForOneOrderWithoutButton
import com.example.front.components.OrderCard
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.viewmodels.orderinfo.OrderInfoViewModel

@Composable
fun OrderScreen (navController : NavHostController, orderId: Int, orderInfoViewModel: OrderInfoViewModel){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    LaunchedEffect(Unit)
    {
        orderInfoViewModel.getOrderInfo(orderId);
    }
    Sidebar(
        drawerState,
        navController,
        orderInfoViewModel.dataStoreManager
    )
    {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background))
    {
        item {
            SmallElipseAndTitle(title = "Order Info", drawerState = drawerState)
        }
        if (orderInfoViewModel.state.value.isLoading) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize().padding(top = 170.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {
                    CircularProgressIndicator()
                }
            }
        } else if (orderInfoViewModel.state.value.error.contains("NotFound")) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 170.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nofound),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                    androidx.compose.material3.Text(
                        "No orders found",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        } else {
            item {
                OrderInfo(orderInfoViewModel)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                OrderCards(navController, orderInfoViewModel)
            }
            item {
                OrderShippingInformation(orderInfoViewModel)
            }
        }
    }
    }
}

@Composable
fun OrderShippingInformation(orderInfoViewModel: OrderInfoViewModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Order information", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color=MaterialTheme.colorScheme.onSurface, fontSize = 25.sp))
        Spacer(modifier = Modifier.height(25.dp))
        Row(

        )
        {
            androidx.compose.material3.Text("Shipping Address: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text(orderInfoViewModel.state.value.orderInfo!!.shippingAddress!!, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            androidx.compose.material3.Text("Delivery Method: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text(orderInfoViewModel.state.value.orderInfo!!.deliveryMethod!!, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
        }
        if(orderInfoViewModel.state.value.orderInfo!!.paymentMethod != null)
        {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                androidx.compose.material3.Text("Payment Method: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
                androidx.compose.material3.Text(orderInfoViewModel.state.value.orderInfo!!.paymentMethod!!, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun OrderCards(navController : NavHostController, orderInfoViewModel: OrderInfoViewModel) {
    val cardList = orderInfoViewModel.state.value.orderInfo!!.items

    if (cardList != null) {
        cardList.forEach { item ->
            Log.d("Item", item.toString())
            CardForOneOrderWithoutButton(
                title = item.name,
                seller = item.shop!!,
                imageResource = item.image,
                navController = navController,
                id = orderInfoViewModel.state.value.orderInfo!!.id!!,
                price = "${item.price} RSD",
                total = "${item.quantity} ${item.metric}"
            )
        }
    }

}

@Composable
fun OrderInfo(orderInfoViewModel: OrderInfoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text("Order "+orderInfoViewModel.state.value.orderInfo!!.id, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color=MaterialTheme.colorScheme.onSurface, fontSize = 25.sp),modifier = Modifier.weight(2f))
            Text(orderInfoViewModel.state.value.orderInfo!!.createdOn.toString(), style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            androidx.compose.material3.Text("Quantity: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text(orderInfoViewModel.state.value.orderInfo!!.quantity.toString()+ " products", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Amount: ", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color = MaterialTheme.colorScheme.primary))
                Text(orderInfoViewModel.state.value.orderInfo!!.amount.toString()+" RSD", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }
            Text(orderInfoViewModel.state.value.orderInfo!!.status!!, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary))
        }
    }
}
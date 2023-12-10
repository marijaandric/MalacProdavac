package com.example.front.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.components.CardButton
import com.example.front.components.CardForOneOrderWithoutButton
import com.example.front.components.OrderCard
import com.example.front.components.SmallElipseAndTitle

@Composable
fun OrderScreen (navController : NavHostController){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background))
    {
        item{
            SmallElipseAndTitle(title = "Order Info", drawerState = drawerState)
        }
        item{
            OrderInfo()
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            OrderCards(navController)
        }
        item{
            OrderShippingInformation()
        }
    }
}

@Composable
fun OrderShippingInformation() {
    Column(
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Order information", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color=MaterialTheme.colorScheme.onSurface, fontSize = 25.sp))
        Spacer(modifier = Modifier.height(25.dp))
        Row(

        )
        {
            androidx.compose.material3.Text("Shipping Address: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text("Vlastimira Petrovica 9, Milocaj, Kraljevo, Srbija", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            androidx.compose.material3.Text("Delivery Method: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text("In-store Pickup", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun OrderCards(navController : NavHostController) {
    CardForOneOrderWithoutButton(title = "Jagode", seller="Domacinstvo Boskovic",imageResource = null, navController = navController, id = 1, price="90,00 RSD", total="20kg")
    CardForOneOrderWithoutButton(title = "Jagode", seller="Domacinstvo Boskovic",imageResource = null, navController = navController, id = 1, price="90,00 RSD", total="20kg")

}

@Composable
fun OrderInfo() {
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
            Text("Order No1", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color=MaterialTheme.colorScheme.onSurface, fontSize = 25.sp),modifier = Modifier.weight(2f))
            Text("10-19-2023", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            androidx.compose.material3.Text("Quantity: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
            androidx.compose.material3.Text(2.toString()+ " products", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
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
                Text("4259 RSD", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }
            Text("Delivered", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary))
        }
    }
}
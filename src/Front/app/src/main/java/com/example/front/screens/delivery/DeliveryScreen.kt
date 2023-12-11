package com.example.front.screens.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.navigation.Screen
import com.example.front.viewmodels.delivery.DeliveryViewModel

@Composable
fun DeliveryScreen(navHostController: NavHostController, deliveryViewModel: DeliveryViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navHostController,
        deliveryViewModel.dataStoreManager
    ) {
        val deliveryRoutes = listOf(
            DeliveryRoute("Kragujevac - Kraljevo", "26/11/2023", "09:00"),
            DeliveryRoute("Kragujevac - Batočina", "28/11/2023", "11:00")
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Set the background color to white for the whole screen
        ) {
            SmallElipseAndTitle("Deliveries", drawerState)
            LazyColumn {
                items(deliveryRoutes) { route ->
                    DeliveryRouteCard(route, navHostController)
                }
            }
        }
    }
}

@Composable
fun DeliveryRouteCard(route: DeliveryRoute, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click here */ },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xF1F1F1)
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = route.name,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${route.date}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Start time: ${route.startTime}")
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = {
                        navHostController.navigate(
                            Screen.Route.route
                        )
                    })
            )
        }
    }
}


data class DeliveryRoute(
    val name: String,
    val date: String,
    val startTime: String
)
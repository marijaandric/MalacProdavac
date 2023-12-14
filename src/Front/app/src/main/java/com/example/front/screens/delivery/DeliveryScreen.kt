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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.DTO.Trip
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
        LaunchedEffect(Unit) {
            deliveryViewModel.getRouteDetails(deliveryViewModel.dataStoreManager.getUserIdFromToken())
        }
        val trips = deliveryViewModel.state.value.details

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SmallElipseAndTitle("Deliveries", drawerState)

            // Check if trips is not null before rendering LazyColumn
            trips?.let {
                LazyColumn {
                    items(it) { trip ->
                        DeliveryRouteCard(trip, navHostController)
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryRouteCard(trip: Trip, navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to the detailed view of the route
                navHostController.navigate("${Screen.Route.route}/${trip.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
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
                    text = trip.startAddress + " - " + trip.endAddress,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${trip.createdOn}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Start time: ${trip.startTime}")
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
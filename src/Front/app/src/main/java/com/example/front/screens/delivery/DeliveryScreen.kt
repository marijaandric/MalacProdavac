package com.example.front.screens.delivery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.CardButton
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.Tabs
import com.example.front.model.DTO.ReqForDeliveryPersonDTO
import com.example.front.model.DTO.Trip
import com.example.front.navigation.Screen
import com.example.front.viewmodels.delivery.DeliveryViewModel

@Composable
fun DeliveryScreen(navHostController: NavHostController, deliveryViewModel: DeliveryViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedColumnIndex by remember {
        mutableStateOf(true)
    }
    Sidebar(
        drawerState,
        navHostController,
        deliveryViewModel.dataStoreManager
    ) {
        LaunchedEffect(Unit) {
            deliveryViewModel.getRouteDetails(deliveryViewModel.dataStoreManager.getUserIdFromToken())
            deliveryViewModel.dataStoreManager.getUserIdFromToken()
                ?.let { deliveryViewModel.getReqForDelivery(it) }
        }
        val trips = deliveryViewModel.state.value.details

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SmallElipseAndTitle("Deliveries", drawerState)

            Tabs(
                onShopsSelected = { selectedColumnIndex=true },
                onFavoritesSelected = { selectedColumnIndex=false },
                selectedColumnIndex = selectedColumnIndex,
                firstTab = "Routes",
                secondTab = "Requests",
                isFilters = false
            )
            if(selectedColumnIndex)
            {
                if(trips == null)
                {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 170.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.nofound), contentDescription = null, modifier = Modifier.size(200.dp))
                        Text("No routes found", style = MaterialTheme.typography.titleSmall)
                    }
                }
                else{
                    trips?.let {
                        LazyColumn {
                            items(it) { trip ->
                                DeliveryRouteCard(trip, navHostController)
                            }
                        }
                    }
                }
            }else{
                if(!deliveryViewModel.stateReq.value.isLoading && deliveryViewModel.stateReq.value.req != null)
                {
                    val yourDataList = deliveryViewModel.stateReq.value.req

                    if(yourDataList != null)
                    {
                        LazyColumn {
                            items(yourDataList) { yourData ->
                                YourDataCard(yourData = yourData)
                            }
                        }
                    }
                }
                else{
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 170.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.nofound), contentDescription = null, modifier = Modifier.size(200.dp))
                        Text("No requests found", style = MaterialTheme.typography.titleSmall)
                    }
                }

            }

        }
    }
}

@Composable
fun YourDataCard(yourData: ReqForDeliveryPersonDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "ID: ${yourData.id}", style=MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.secondary))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Locations: ${yourData.locations}", style=MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Normal))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = yourData.startAddress+" - "+yourData.endAddress, style=MaterialTheme.typography.displaySmall.copy(color = Color.Black))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardButton(text = "Accept", onClick = { /*TODO*/ }, width = 0.5f, modifier = Modifier.height(40.dp), color = MaterialTheme.colorScheme.primary)
                CardButton(text = "Decline", onClick = { /*TODO*/ }, width = 0.9f, modifier = Modifier.height(40.dp), color = MaterialTheme.colorScheme.secondary)
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
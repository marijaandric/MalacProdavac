package com.example.front.screens.RequestsForShopScreen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.model.DTO.RequestsForShopDTO
import com.example.front.viewmodels.requestsshop.RequestsForShopState
import com.example.front.viewmodels.requestsshop.RequestsForShopViewModel
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.DTO.DeliveryPersonDTO
import java.text.SimpleDateFormat
import java.util.Locale

enum class ShopTabs {
    Requests, Orders
}

@Composable
fun RequestsForShopScreen(
    viewModel: RequestsForShopViewModel,
    navHostController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.dataStoreManager.getUserIdFromToken()?.let { viewModel.getRequestsForShopDTO(it) }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(ShopTabs.Requests) }

    Sidebar(
        drawerState = drawerState,
        navController = navHostController,
        dataStoreManager = viewModel.dataStoreManager
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmallElipseAndTitle(title = "Orders", drawerState)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.Center
            ) {
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    backgroundColor = Color(0xFFF1F1F1),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    ShopTabs.values().forEach { tab ->
                        Tab(
                            selected = tab == selectedTab,
                            onClick = { setSelectedTab(tab) },
                            text = { Text(tab.name) }
                        )
                    }
                }
            }


            when (selectedTab) {
                ShopTabs.Requests -> RequestsTabContent(viewModel)
                ShopTabs.Orders -> Orders()
            }
        }
    }
}

data class Order(
    val orderNumber: String,
    val date: String,
    val quantity: Int,
    val totalAmount: String,
    val status: String
)

@Composable
fun OrdersScreen(orders: List<Order>) {
    LazyColumn(modifier = Modifier.fillMaxWidth(0.9f), horizontalAlignment = Alignment.CenterHorizontally) {
        items(orders) { order ->
            OrderCard(order)
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Order ${order.orderNumber}", fontWeight = FontWeight.Bold)
                Text(text = order.date)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Quantity: ${order.quantity}")
            Text(text = "Total Amount: ${order.totalAmount}")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Handle details click */ },
                    colors = ButtonDefaults.buttonColors(Color(0xFFE48359))
                ) {
                    Text("Details", color = Color.White)
                }
                Text(text = order.status, color = when (order.status) {
                    "Delivered" -> Color(0xFFE48359)
                    "Processing" -> Color(0xFF294E68)
                    "Pending" -> Color(0xFF77A7CA)
                    else -> Color.Gray
                })
            }
        }
    }
}

@Composable
fun Orders() {
    val orders = listOf(
        Order("1037088", "10-19-2023", 2, "1425 RSD", "Processing"),
        Order("1049718", "10-19-2023", 2, "4525 RSD", "Delivered"),
        Order("2549710", "10-16-2023", 1, "630 RSD", "Pending")
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OrdersScreen(orders = orders)
    }
}

@Composable
fun RequestsTabContent(viewModel: RequestsForShopViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (viewModel.state.value.requests.isEmpty()) Arrangement.Center else Arrangement.Top,
        horizontalAlignment = if (viewModel.state.value.requests.isEmpty()) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            RequestsList(viewModel.state,viewModel)
        }
    }
}

@Composable
fun RequestsList(state: State<RequestsForShopState>, viewModel: RequestsForShopViewModel) {
    if (state.value.requests.isEmpty())
        Image(
            painter = painterResource(id = R.drawable.requests),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    else
        LazyColumn {
            items(state.value.requests) { request ->
                RequestCard(request, viewModel)
            }
        }
}


@Composable
fun RequestCard(request: RequestsForShopDTO,viewModel: RequestsForShopViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { viewModel.getDeliveriesPersonForRequest(request.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = request.locations,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDate(request.createdOn),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = request.startAddress,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Text(
                text = request.endAddress,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    if (viewModel.showDeliveryModal.value) {
        DeliveryPersonModal(
            deliveryPeople = viewModel.stateDelivery.value.persons,
            onDismissRequest = { viewModel.hideDeliveryModal() },
            onSelectDeliveryPerson = { deliveryPersonId ->
                viewModel.performRequestWithSelectedPerson(request.id,deliveryPersonId)
                viewModel.hideDeliveryModal()
            }
        )
    }
}

@Composable
fun DeliveryPersonModal(
    deliveryPeople: List<DeliveryPersonDTO>,
    onDismissRequest: () -> Unit,
    onSelectDeliveryPerson: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn {
                items(deliveryPeople) { person ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onSelectDeliveryPerson(person.deliveryPersonId) },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Delivery Person: ${person.deliveryPerson}")
                            Text(text = "ID: ${person.deliveryPersonId}")
                            Text(text = "Price: ${person.price}")
                            Text(text = "Date: ${person.date}")
                            Text(text = "Route Divergence: ${person.closestRouteDivergence}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onSelectDeliveryPerson(person.deliveryPersonId) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Select This Delivery Person")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val parsedDate = parser.parse(dateString)
        formatter.format(parsedDate)
    } catch (e: Exception) {
        dateString
    }
}

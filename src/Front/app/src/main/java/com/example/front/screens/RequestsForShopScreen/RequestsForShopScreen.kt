package com.example.front.screens.RequestsForShopScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.model.DTO.RequestsForShopDTO
import com.example.front.viewmodels.requestsshop.RequestsForShopState
import com.example.front.viewmodels.requestsshop.RequestsForShopViewModel
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RequestsForShopScreen(
    viewModel: RequestsForShopViewModel,
    navHostController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.dataStoreManager.getUserIdFromToken()?.let { viewModel.getRequestsForShopDTO(it) }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState = drawerState,
        navController = navHostController,
        dataStoreManager = viewModel.dataStoreManager
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = if (viewModel.state.value.requests.isEmpty()) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = if (viewModel.state.value.requests.isEmpty()) Alignment.CenterHorizontally else Alignment.Start
        ) {
            SmallElipseAndTitle(title = "Orders", drawerState)
            Column(modifier = Modifier.padding(16.dp)) {
                RequestsList(viewModel.state)
            }
        }

    }
}

@Composable
fun RequestsList(state: State<RequestsForShopState>) {
    if (state.value.requests.isEmpty())
        Image(
            painter = painterResource(id = R.drawable.requests),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    else
        LazyColumn {
            items(state.value.requests) { request ->
                RequestCard(request)
            }
        }
}

@Composable
fun RequestCard(request: RequestsForShopDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {  },
        shape = RoundedCornerShape(8.dp), // Rounded corners for a softer look
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

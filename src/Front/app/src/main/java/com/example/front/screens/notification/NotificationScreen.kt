package com.example.front.screens.notification

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.Paginator
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.notification.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController, viewModel: NotificationViewModel) {

    var currentPage by remember { mutableStateOf(1) }
    var totalPages = 1

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(Unit) {
        viewModel.dataStoreManager.getUserIdFromToken()
            ?.let { viewModel.getNotifications(it, listOf(), currentPage) }
    }

    Sidebar(
        drawerState,
        navController,
        viewModel.dataStoreManager
    ) {
        if (viewModel.state.value.isLoading) {
            CircularProgressIndicator()
        } else {
            totalPages = viewModel.statePageCount.value
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                SmallElipseAndTitle("Notifications", drawerState)
                //Spacer(modifier = Modifier.height(150.dp))
                TypeOfNotifications()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Today",
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Text(
                        text = "Clear all",
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                ) {
                    items(viewModel.state.value.notifications) { notification ->
                        NotificationCard(title = notification.title, notification.text) {

                        }
                    }
                }
                Paginator(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageSelected = { newPage ->
                        if (newPage in 1..totalPages) {
                            currentPage = newPage
                        }
                    }
                )
            }
        }
    }
}

data class NotificationCategory(
    val id: Int,
    val text: String
)

@Composable
fun TypeOfNotifications() {
    val items = listOf(
        NotificationCategory(0, "Info"),
        NotificationCategory(1, "Rate Person"),
        NotificationCategory(2, "Product"),
        NotificationCategory(3, "Shop"),
        NotificationCategory(4, "Route"),
        NotificationCategory(5, "Req"),
        NotificationCategory(6, "Order"),
        NotificationCategory(7, "Rate Product"),
        NotificationCategory(8, "Rate Shop")
    )

    LazyRow {
        items(items) { item ->
            ItemType(item.id, text = item.text)
        }
    }
}

@Composable
fun ItemType(id: Int, text: String) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Text(text = text, modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationCard(title: String, body: String, onDismissed: () -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { 300.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    if (swipeableState.currentValue == 0) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(130.dp)
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                ),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_people),
                        contentDescription = "Left Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "$title")
                    Icon(
                        painter = painterResource(id = R.drawable.carbon_dot_mark),
                        contentDescription = "Right Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                Text(
                    text = "$body",
                    modifier = Modifier
                        .padding(10.dp)
                )

                Text(
                    text = "View",
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(20.dp)
                )
            }
        }
        if (swipeableState.currentValue == 1) {
            onDismissed()
        }
    }
}



package com.example.front.screens.notification

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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.SmallElipseAndTitle
import com.example.front.ui.theme.Typography

@Composable
fun NotificationScreen() {
    SmallElipseAndTitle("Notifications")
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        TypeOfNotifications()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today",
                style = Typography.bodyMedium,
            )
            Text(text = "Clear all", style = Typography.bodyMedium)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                NotificationCard()
            }
            item {
                NotificationCard()
            }
            item {
                NotificationCard()
            }
        }
    }
}

@Composable
fun TypeOfNotifications() {
    // Sample data
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    LazyRow {
        items(items) { item ->
            ItemType(text = item)
        }
    }
}

@Composable
fun ItemType(text: String) {
    Card(
        shape = RoundedCornerShape(10.dp), // Border radius
        modifier = Modifier.padding(8.dp) // Spacing between items
    ) {
        Text(text = text, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun NotificationCard() {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // First row with two icons and a text in the middle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), // Add padding at the bottom
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_people), // Replace with your icon resource
                    contentDescription = "Left Icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Appointment accepted")
                Icon(
                    painter = painterResource(id = R.drawable.carbon_dot_mark), // Replace with your icon resource
                    contentDescription = "Right Icon",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Second row with just text
            Text(
                text = "User vinarija.vuković has accepted your appointment to pick up the product.",
                modifier = Modifier
                    .padding(20.dp) // Padding around the text
            )

            // Third row with a 'View' text in a different color
            Text(
                text = "View",
                color = Color.Blue, // Replace with your preferred color
                modifier = Modifier
                    .padding(20.dp) // Padding around the text
            )
        }
    }
}




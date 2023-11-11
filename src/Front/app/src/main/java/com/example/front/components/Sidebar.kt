@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.front.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.front.R
import kotlinx.coroutines.launch

@Composable
fun Sidebar() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem(icon = painterResource(id = R.drawable.navbar_home), label = "Home", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_cart2), label = "My Cart", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_cart1), label = "My Orders", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_package), label = "Products", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_shop1), label = "Shops", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_bell), label = "Notifications", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_shop2), label = "My Shop", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_message), label = "Messages", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_profile), label = "Delivery Profile", secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_car), label = "Deliveries", secondaryLabel = ""),
    )
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState=drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Header", style = MaterialTheme.typography.headlineLarge)
                }
                items.forEach{item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                        },
                        icon = { Icon(painter = item.icon, contentDescription = null) }
                    )
                }
            }
        },
        content = {
//            Content(
//                onClick = { scope.launch { drawerState.open() } }
//            )
        }
    )
}

data class DrawerItem(
    val icon: Painter,
    val label: String,
    val secondaryLabel: String
)

@Composable
fun Content(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { onClick }) {
            Text(text = "Click to Open")
        }
    }
}
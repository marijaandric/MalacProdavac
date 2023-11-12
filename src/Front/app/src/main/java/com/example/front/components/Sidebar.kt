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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults.colors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun Sidebar(
    navController: NavHostController
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem(icon = painterResource(id = R.drawable.navbar_home), label = "Home", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_cart2), label = "My cart", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_cart1), label = "My orders", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_package), label = "Products", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_shop1), label = "Shops", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_bell), label = "Notifications", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_shop2), label = "My shop", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_message), label = "Messages", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_profile), label = "Delivery profile", route = Screen.Home.route, secondaryLabel = ""),
        DrawerItem(icon = painterResource(id = R.drawable.navbar_car), label = "Deliveries", route = Screen.Home.route, secondaryLabel = ""),
    )
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState=drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(324.dp),
                drawerContainerColor = Color(0xFF294E68)
            ){
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 64.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "Header", style = MaterialTheme.typography.headlineLarge)
//                }
                Spacer(
                    modifier = Modifier
                        .height(56.dp)
                )

                items.forEach{item ->
                    NavigationDrawerItem(
                        modifier = Modifier

                        ,
                        label = { Text(text = item.label) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                            navController.navigate(route = item.route)
                        },
                        icon = { Icon(painter = item.icon, contentDescription = null, modifier = Modifier.height(24.dp)) },
                        colors = colors(
                            unselectedContainerColor = Color(0xFF294E68),
                            selectedContainerColor = Color(0xFF263e52),
                            unselectedTextColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color.White,
                            selectedIconColor = Color.White,
                        ),
                        shape = RectangleShape
                    )
                }

//                Text(
//                    text = "Logout",
//                    modifier = Modifier
//                        .clickable {
//                            //pokaze modal da potvrdi da zeli da se izloguje
//                                //ukloni token
//                            //vodi na Login
//                        }
//                )
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
    val route: String,
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
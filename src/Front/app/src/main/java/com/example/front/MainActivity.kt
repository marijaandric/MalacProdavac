package com.example.front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults.colors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.front.components.DrawerItem
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.navigation.Screen
import com.example.front.navigation.SetupNavGraph
import com.example.front.screens.userprofile.UserProfileScreen
import com.example.front.ui.theme.FrontTheme
import com.example.front.viewmodels.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

    @AndroidEntryPoint
    class MainActivity : ComponentActivity() {
        lateinit var navController: NavHostController
        @Inject lateinit var dataStoreManager: DataStoreManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val logged = runBlocking {
                dataStoreManager.isLoggedIn()
            }

            FrontTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    navController = rememberNavController()


                    val items = listOf(
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_home),
                            label = "Home",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_cart2),
                            label = "My cart",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_cart1),
                            label = "My orders",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_package),
                            label = "Products",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_shop1),
                            label = "Shops",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_bell),
                            label = "Notifications",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_shop2),
                            label = "My shop",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_message),
                            label = "Messages",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_profile),
                            label = "Profile",
                            route = Screen.MyProfile.route,
                            secondaryLabel = ""
                        ),
                        DrawerItem(
                            icon = painterResource(id = R.drawable.navbar_car),
                            label = "Deliveries",
                            route = Screen.Home.route,
                            secondaryLabel = ""
                        ),
                    )

                    if (logged){
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
                                            selected = false,
                                            onClick = {
                                                scope.launch { drawerState.close() }
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

                                    Text(
                                        text = "Logout",
                                        modifier = Modifier
                                            .clickable {
                                                //pokaze modal da potvrdi da zeli da se izloguje
                                                //ukloni token
                                                //vodi na Login
                                                runBlocking {
                                                    dataStoreManager.storeToken("")
                                                }
                                                val intent = Intent(this@MainActivity, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                    )
                                }
                            },
                            content = {
                                SetupNavGraph(navController = navController)
                            }
                        )
                    } else {
                        SetupNavGraph(navController = navController)
                    }

                }
            }
        }
    }
}
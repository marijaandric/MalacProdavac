package com.example.front.screens.myshop

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.navigation.Screen
import com.example.front.viewmodels.myshop.MyShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyShopScreen(navController : NavHostController, myShopViewModel: MyShopViewModel) {
    var userId by remember {
        mutableStateOf(0)
    }
//    LaunchedEffect(Unit) {
//            myShopViewModel.getUserId()?.let { myShopViewModel.getShopId(it); userId = it}
//    }
    LaunchedEffect(Unit) {
        val fetchedUserId = myShopViewModel.getUserId()
        if (fetchedUserId != null) {
            userId = fetchedUserId
            myShopViewModel.getShopId(userId)
        }
    }

    DisposableEffect(myShopViewModel.state.value.shopId) {
        if (!myShopViewModel.state.value.isLoading) {
            val shopId = myShopViewModel.state.value.shopId?.id
            if (shopId != null && shopId != 0) {
                val x = 1
                navController.navigate("${Screen.Shop.route}/$shopId/$x")
            }
        }
        onDispose { /* Cleanup, if needed */ }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        myShopViewModel.dataStoreManager
    ) {
        if (myShopViewModel.state.value.isLoading) {
            //CircularProgressIndicator()
        } else {
            if (myShopViewModel.state.value.shopId!!.id == 0)//true
            {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                ) {
                    SmallElipseAndTitle(title = "My shop", drawerState)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                            .padding(26.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = "It looks like your shop isn't set up yet.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Don't worry, we're here to help you get started.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Click below to begin the setup process and start showcasing your products to the world.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.intro2),
                            contentDescription = null,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        BigBlueButton(
                            text = "Set up shop",
                            onClick = { navController.navigate("setup_shop") },
                            width = 1f,
                            modifier = Modifier
                        )
                    }
                }

            }
//            else{
//                if(myShopViewModel.state.value.shopId != null)
//                {
//                    val x = 1
//                    val shopId = myShopViewModel.state.value.shopId!!.id
//                    navController.navigate("${Screen.Shop.route}/$shopId/$x")
//                }
//            }
        }
    }
}
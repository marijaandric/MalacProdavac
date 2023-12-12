package com.example.front.screens.home

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ProductCard
import com.example.front.components.SellerCard
import com.example.front.components.Sidebar
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.viewmodels.home.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        homeViewModel.getHomeProducts(homeViewModel.getUserId())
        homeViewModel.getHomeShops(homeViewModel.getUserId())
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        homeViewModel.dataStoreManager
    ) {
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Search(drawerState)
            }
            item {
                Sellers(homeViewModel, navController)
            }
            item {
                Products(homeViewModel, navController)
            }
        }
    }
}

data class CardData(
    val id: Int,
    val title: String,
    val description: String,
    val imageResource: String?,
    var isLiked: Boolean
)

@Composable
fun Products(viewModel: HomeViewModel, navController: NavHostController) {
    val state = viewModel.state.value

    val products = state.products?.mapIndexed { index, productsState ->
        CardData(
            id = productsState.id,
            title = productsState.name,
            description = String.format("%.2f", productsState.price)+ " din",
            imageResource = productsState.image,
            isLiked = false
        )
    }?.toList() ?: emptyList()


    Column(
        modifier = Modifier
            .padding(16.dp, end = 0.dp, top = 20.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Recommended products",  style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.padding(bottom = 10.dp))
        if (viewModel.state.value.isLoading) {
            Box(
                modifier= Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            {
                CircularProgressIndicator()
            }
        } else {
            Column(
                //modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                products.forEach { cardData ->
                    val imageResource = cardData.imageResource ?: "imageplaceholder.jpg"

                    ProductCard(
                        title = cardData.title,
                        price = cardData.description,
                        imageResource = imageResource,
                        navController,
                        cardData.id
                    )
                }
            }
        }

    }
}

@Composable
fun Sellers(viewModel: HomeViewModel, navController: NavHostController) {
    val state = viewModel.stateShop.value

    val sellers = state.shops?.mapIndexed { index, shopState ->
        CardData(
            id = shopState.id,
            title = shopState.name,
            description = shopState.address,
            imageResource = shopState.image.toString(),
            isLiked = shopState.liked
        )
    }?.toList() ?: emptyList()


    Column(
        modifier = Modifier
            .padding(16.dp, end = 0.dp)
    ) {
        Text(text = "Recommended sellers", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(bottom = 10.dp),)
        if(viewModel.stateShop.value.isLoading)
        {
            Box(
                modifier= Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            {
                CircularProgressIndicator()
            }
        }
        else{
            LazyRow(
                modifier = Modifier.heightIn(100.dp, 600.dp)
            ) {
                itemsIndexed(sellers) { index, cardData ->
                    cardData.imageResource?.let { imageResource ->
                        SellerCard(
                            title = cardData.title,
                            author = cardData.description,
                            imageResource = imageResource,
                            isLiked = cardData.isLiked,
                            onClick = {
                                viewModel.updateLikeStatus(index, !cardData.isLiked)
                                viewModel.changeLikedState(cardData.id)
                            },
                            navController = navController,
                            id = cardData.id
                        )
                    } ?: run {
                        // Use a default image placeholder when cardData.imageResource is null
                        SellerCard(
                            title = cardData.title,
                            author = cardData.description,
                            imageResource = "imageplaceholder.jpg", // Replace with your default image placeholder
                            isLiked = cardData.isLiked,
                            onClick = {
                                viewModel.updateLikeStatus(index, !cardData.isLiked)
                                viewModel.changeLikedState(cardData.id)
                            },
                            navController = navController,
                            id = cardData.id
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(drawerState: DrawerState) {
    var value by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            painter = painterResource(id = R.drawable.elipse),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Column()
        {
            Row(
                modifier = Modifier.padding(top = 35.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Search icon",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { scope.launch { drawerState.open() }},
                    tint = MaterialTheme.colorScheme.background
                )
                Text(getGreetingMessage(),style=MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.background))
            }
            Image(
                painter = painterResource(id = R.drawable.homepage),
                contentDescription = "HomePageAction",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 35.dp)

            )
        }
    }
}

fun getGreetingMessage(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hour in 6..11 -> "Good morning!"
        hour in 12..16 -> "Good afternoon!"
        hour in 17..20 -> "Good evening!"
        else -> "Good night!"
    }
}












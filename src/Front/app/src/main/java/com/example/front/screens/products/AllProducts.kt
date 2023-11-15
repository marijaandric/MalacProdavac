package com.example.front.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ProductCard
import com.example.front.components.SearchTextField
import com.example.front.screens.home.CardData
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.home.HomeViewModel

@Composable
fun AllProducts(navController: NavHostController, homeViewModel: HomeViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Search()
            Text(
                text = "Discover the Best Products",
                style = Typography.titleMedium,
                modifier = Modifier.padding(start = 30.dp,end = 30.dp)
            )
            Products(homeViewModel,navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search() {
    var value by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.rsz_elipse),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column {
            Row(
                modifier = Modifier
                    .padding(top = 15.dp, start = 15.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Search icon",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp),
                    tint = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "Products",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f) // This will make the Text take the remaining space and be centered
                        .align(Alignment.CenterVertically)
                        .padding(end = 75.dp)
                )
            }
        }
    }
    Column(
        modifier = Modifier.padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            SearchTextField(
                valuee = value,
                placeh = "Search products",
                onValueChangee = { value = it },
                modifier = Modifier.width(280.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.reverse),
                contentDescription = "HomePageAction",
                modifier = Modifier
                    .padding(20.dp)
                    .scale(3.2f)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = "HomePageAction",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(20.dp)
                    .scale(3.2f)
                    .align(Alignment.CenterVertically)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.productspage),
            contentDescription = "HomePageAction",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 35.dp)

        )
    }
}

@Composable
fun Products(viewModel: HomeViewModel, navController: NavHostController) {
    val state = viewModel.state.value

    val products = state.products?.mapIndexed { index, productsState ->
        CardData(
            title = productsState.name,
            description = productsState.price.toString() + " din",
            imageResource = R.drawable.imageplaceholder,
            id = productsState.id,
            isLiked = false
        )
    }?.toList() ?: emptyList()

    Column(
        modifier = Modifier
            .padding(16.dp, end = 0.dp, top = 20.dp)
    ) {
        if (viewModel.state.value.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.heightIn(100.dp, 600.dp)
            ) {
                items(products) { cardData ->
                    ProductCard(
                        title = cardData.title,
                        price = cardData.description,
                        imageResource = cardData.imageResource,
                        navController,
                        cardData.id
                    )
                }
            }
        }

    }
}

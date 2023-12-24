package com.example.front.screens.products

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.FilterDialogAllProducts
import com.example.front.components.ProductCard
import com.example.front.components.SearchTextField
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.SortDialogForAllProducts
import com.example.front.components.Tabs
import com.example.front.screens.home.CardData
import com.example.front.screens.sellers.CardGrid
import com.example.front.screens.sellers.MapFilters
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.home.HomeViewModel
import com.example.front.viewmodels.oneshop.OneShopViewModel
import com.example.front.viewmodels.products.ProductsViewModel
import com.example.front.viewmodels.shops.ShopsViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProducts(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    oneShopViewModel: OneShopViewModel
) {
    var selectedColumnIndex by remember {
        mutableStateOf(true)
    }
    var all by remember {
        mutableStateOf(false)
    }
    var fav by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit)
    {
        productsViewModel.getUserId()?.let { productsViewModel.getProducts(it, listOf(),null, null, null, null, 0, null, 1, false, null, null ) }
        productsViewModel.getUserId()?.let { productsViewModel.getProducts(it, listOf(),null, null, null, null, 0, null, 1, true, null, null ) }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        productsViewModel.dataStoreManager
    ) {
        LazyColumn(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()) {
            item{
                SmallElipseAndTitle("All products", drawerState)
            }
            item{
                SearchAndFilters(productsViewModel = productsViewModel)
            }
            item{
                Tabs(
                    onShopsSelected = { selectedColumnIndex = true },
                    onFavoritesSelected = { selectedColumnIndex = false },
                    selectedColumnIndex = selectedColumnIndex,
                    firstTab = "Products",
                    secondTab = "Your Favorites",
                    isFilters = false
                )
                Spacer(modifier = Modifier.height(10.dp))

            }
            item{
                if(selectedColumnIndex && productsViewModel.state.value.error.isEmpty())
                {
                    Image(
                        painter = painterResource(id = R.drawable.products),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, bottom = 0.dp)
                    )
                }
            }
            item{
                if(selectedColumnIndex && productsViewModel.state.value.error.isEmpty())
                    Text("Explore the Finest Selection of Products!", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(20.dp,top=0.dp,bottom = 16.dp))
                Products(productsViewModel, navController, selectedColumnIndex)
            }
        }
    }
}

@Composable
fun SearchAndFilters(productsViewModel: ProductsViewModel) {
    var value by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isOverlayVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    )
    {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        {
            SearchTextField(
                valuee = value,
                placeh = "Search products",
                onValueChangee = { value = it; productsViewModel.Search(it) },
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            Image(
                // ako budemo imali dark i light ovde mozda neki if i promena slike
                painter = painterResource(id = R.drawable.filters),
                contentDescription = "Search icon",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { showDialog = true },
            )
            Image(
                // ako budemo imali dark i light ovde mozda neki if i promena slike
                painter = painterResource(id = R.drawable.sort),
                contentDescription = "Sort icon",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(50.dp, 40.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { isOverlayVisible = true },
            )
        }
    }

    if (showDialog) {
        FilterDialogAllProducts(onDismiss = { showDialog = false }, productsViewModel)
    }
    if (isOverlayVisible) {
        SortDialogForAllProducts(onDismiss = { isOverlayVisible = false }, productsViewModel)
    }
}

@Composable
fun Products(viewModel: ProductsViewModel, navController: NavHostController, isFav:Boolean) {
    var state = viewModel.state.value
    if(!isFav)
    {
        state = viewModel.stateFav.value
    }

    val products = state.products?.mapIndexed { index, productsState ->
        CardData(
            title = productsState.name,
            description = DecimalFormat("0.00").format(productsState.price).toString() + " din",
            imageResource = productsState.image,
            id = productsState.id,
            isLiked = false
        )
    }?.toList() ?: emptyList()
    Column(
        modifier = Modifier
            .padding(16.dp, end = 0.dp, top = 20.dp)
    ) {
        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top=30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        else if(state.error.isNotEmpty())
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nofound),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(top = 55.dp, bottom = 16.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    Text("No products found", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
        else {
            Column() {
                for (cardData in products) {
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

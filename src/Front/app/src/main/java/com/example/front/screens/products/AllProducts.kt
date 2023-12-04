package com.example.front.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.ProductCard
import com.example.front.components.SearchTextField
import com.example.front.components.Sidebar
import com.example.front.components.Tabs
import com.example.front.screens.home.CardData
import com.example.front.screens.sellers.CardGrid
import com.example.front.screens.sellers.MapFilters
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.home.HomeViewModel
import com.example.front.viewmodels.shops.ShopsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProducts(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    shopsViewModel: ShopsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        homeViewModel.dataStoreManager
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Search(shopsViewModel, drawerState)
                Text(
                    text = "Discover the Best Products",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                )
                Products(homeViewModel, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(shopsViewModel: ShopsViewModel, drawerState: DrawerState) {
    var value by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
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
                        .padding(start = 10.dp)
                        .clickable { scope.launch { drawerState.open() } },
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
        SearchAndFilters(shopsViewModel)

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
fun SearchAndFilters(shopsViewModel: ShopsViewModel) {
    var value by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isOverlayVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    )
    {
        SearchTextField(
            valuee = value,
            placeh = "Search sellers",
            onValueChangee = { value = it; shopsViewModel.Search(value) },
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

    if (showDialog) {
        FiltersDialog(onDismiss = { showDialog = false }, shopsViewModel)
    }
    if (isOverlayVisible) {
        Overlay(onDismiss = { isOverlayVisible = false }, shopsViewModel)
    }
}

@Composable
fun Overlay(onDismiss: () -> Unit, shopsViewModel:ShopsViewModel) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onDismiss()
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(top = 5.dp)
                    .align(Alignment.Center),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        "Sort", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier
                            .padding(bottom = 40.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        "Default", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(0) }
                    )
                    Text(
                        "Rating (lowest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(1) }
                    )
                    Text(
                        "Rating (highest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(2) }
                    )
                    Text(
                        "Alphabetically (ascending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(3) }
                    )
                    Text(
                        "Alphabetically (descending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(4) }
                    )

                }
            }
        }
    }
}

@Composable
fun FiltersDialog(onDismiss: () -> Unit, shopsViewModel: ShopsViewModel) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    var selectedColumnIndex by remember { mutableStateOf(true) }

    var selectedCategories by remember { mutableStateOf(shopsViewModel.filtersState.value.categories!!.toMutableList()) }
    var review by remember { mutableStateOf(shopsViewModel.filtersState.value.rating) }
    var open by remember { mutableStateOf(if (shopsViewModel.filtersState.value.open != null) shopsViewModel.filtersState.value.open else false) }
    var location by remember { mutableStateOf(if (shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()) }
    var range by remember { mutableStateOf(if (shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f) }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        shopsViewModel.changeCategories(selectedCategories)
                        shopsViewModel.changeRating(review)
                        shopsViewModel.changeOpen(open)
                        shopsViewModel.changeLocation(location)
                        shopsViewModel.changeRange(range.toInt())
                        onDismiss()
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(top = 5.dp)
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .clickable {
                                    shopsViewModel.changeCategories(selectedCategories)
                                    shopsViewModel.changeRating(review)
                                    shopsViewModel.changeOpen(open)
                                    shopsViewModel.changeLocation(location)
                                    shopsViewModel.changeRange(range.toInt())
                                    onDismiss()
                                }
                        )
                        Text(
                            "Filters",
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            "Reset",
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .clickable {
                                    shopsViewModel.changeCategories(selectedCategories)
                                    shopsViewModel.changeRating(review)
                                    shopsViewModel.changeOpen(open)
                                    shopsViewModel.changeLocation(location)
                                    shopsViewModel.changeRange(range.toInt())
                                    shopsViewModel.withoutFilters()
                                }
                        )
                    }

                    //tabs
                    Tabs(
                        onShopsSelected = { selectedColumnIndex = true },
                        onFavoritesSelected = { selectedColumnIndex = false },
                        selectedColumnIndex = selectedColumnIndex,
                        "Details",
                        "Location",
                        true
                    )

                    if (selectedColumnIndex) {
                        CardGrid(onCategorySelected = { selectedCategories = it },
                            onReviewSelected = { review = it },
                            onOpenChanged = { open = it }, shopsViewModel
                        )
                    } else {
                        MapFilters(
                            onSearchChange = { location = it },
                            onSliderChange = { range = it },
                            shopsViewModel
                        )
                    }
                    BigBlueButton(text = "Show Results", onClick = {
                        shopsViewModel.DialogFilters(
                            selectedCategories,
                            review,
                            open,
                            location,
                            range.toInt()
                        )
                        onDismiss()
                    }, width = 1f, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun Products(viewModel: HomeViewModel, navController: NavHostController) {
    val state = viewModel.state.value

    val products = state.products?.mapIndexed { index, productsState ->
        CardData(
            title = productsState.name,
            description = productsState.price.toString() + " din",
            imageResource = productsState.image,
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
                    cardData.imageResource?.let {
                        ProductCard(
                            title = cardData.title,
                            price = cardData.description,
                            imageResource = it,
                            navController,
                            cardData.id
                        )
                    }
                }
            }
        }

    }
}

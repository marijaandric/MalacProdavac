package com.example.front.screens.shop

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.FilterDialogProducts
import com.example.front.components.SearchTextField
import com.example.front.components.ShopProductCard
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.SortDialog
import com.example.front.components.ToggleImageButton
import com.example.front.components.ToggleImageButtonFunction
import com.example.front.screens.categories.ClickableCard
import com.example.front.screens.sellers.FiltersDialog
import com.example.front.viewmodels.oneshop.OneShopViewModel
import com.example.front.viewmodels.shops.ShopsViewModel
import kotlinx.coroutines.delay

@Composable
fun ShopScreen(navController: NavHostController, shopViewModel: OneShopViewModel, shopId: Int) {
    var selectedColumnIndex by remember { mutableStateOf(true) }
    var id : Int = 0
    LaunchedEffect(Unit) {
        shopViewModel.getUserId()
            ?.let {
                shopViewModel.getShopDetails(it, shopId)
                id = it
            }
        shopViewModel.getUserId()
            ?.let {
                shopViewModel.getProducts(it, listOf(),null,null,null,null,0,null,1,shopId, false,null,null )
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    )
    {
        item{
            SmallElipseAndTitle(title = "Shop")
        }
        if(shopViewModel.state.value.isLoading)
        {
            item{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator()
                }
            }
        }
        else{
            item{
                //shop for user
                ProfilePic(shopViewModel,id)
            }
            item{
                ShopInfo(shopViewModel, shopId)
            }
        }

    }
}


@Composable
fun ShopInfo(shopViewModel: OneShopViewModel, shopId:Int) {
    var isImageClicked by remember { mutableStateOf(true) }
    var firstTime by remember { mutableStateOf(true) }

    val scaleImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.1.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val scaleImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 1.1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 0.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 0.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.jakoprovidnaelipsa),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage1.value)
                    .graphicsLayer(scaleX = scaleImage1.value, scaleY = scaleImage1.value)
            )
            Image(
                painter = painterResource(id = R.drawable.jakoprovidnaelipsa2),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage2.value)
                    .graphicsLayer(scaleX = scaleImage2.value, scaleY = scaleImage2.value)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 15.dp, end = 16.dp)
                    .zIndex(2f)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(2f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Info",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(isImageClicked) 25.sp else 20.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = true
                        }
                    )
                    Text(
                        "Products",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(!isImageClicked) 25.sp else 20.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = false
                        }
                    )
                }

                Info(isImageClicked,shopViewModel)
                Products(isImageClicked,shopViewModel, shopId)

            }
        }
    }
}

@Composable
fun Products(isImageClicked: Boolean,shopViewModel: OneShopViewModel,shopId:Int) {
    var showElseText by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember {
        mutableStateOf(false)
    }

    var value by remember {
        mutableStateOf("")
    }

    val data = listOf(
        1,2,3,4
    )

    LaunchedEffect(!isImageClicked) {
        delay(200)
        showElseText = true
    }
    if(showElseText && !isImageClicked) {
        Column {
            Row(
                modifier = Modifier.padding(top=50.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                SearchTextField(valuee = value, placeh = "Search products", onValueChangee = {value = it; shopViewModel.Search(value, shopId)}, modifier = Modifier.fillMaxWidth(0.75f))
                Image(
                    painter = painterResource(id = R.drawable.filters),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                        .clickable {
                            showDialog = true
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                        .clickable { showSortDialog = true }
                )
            }
            if(shopViewModel.stateProduct.value.isLoading)
            {
                CircularProgressIndicator()
            }
            else if(shopViewModel.stateProduct.value.error.contains("NotFound"))
            {
                androidx.compose.material3.Text(
                    "No shops found",
                    style = MaterialTheme.typography.titleSmall, modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            else{
                LazyVerticalGrid (columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 25.dp)
                        .heightIn(400.dp, 600.dp)
                ) {
                    shopViewModel.stateProduct.value.products?.let { products ->
                        items(products) { product ->
                            ShopProductCard(
                                imageRes = product.image,
                                text = product.name,
                                price = "${product.price} din/kom",
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }

        }
    }
    if (showDialog) {
        FilterDialogProducts(onDismiss = { showDialog = false }, shopViewModel, shopId)
    }
    if (showSortDialog) {
        SortDialog(onDismiss = { showSortDialog = false }, shopViewModel, shopId)
    }
}

@Composable
fun Info(isImageClicked: Boolean, shopViewModel: OneShopViewModel) {
    val state = shopViewModel.state.value.shop
    var showText by remember { mutableStateOf(false) }
    var firstTime by remember { mutableStateOf(true) }
    if(isImageClicked) {
        var showText by remember { mutableStateOf(false) }

        LaunchedEffect(isImageClicked) {
            delay(200)
            showText = true
            firstTime = false
        }
        if(showText || firstTime) {

            Column(
                modifier = Modifier
                    .padding(top = 50.dp, start = 5.dp)
                    .zIndex(2f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -- SHOP CATEGORIES
                    Image(
                        painter = painterResource(id = R.drawable.categories),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Shop categories",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }
                Text(
                    text = state!!.categories.joinToString(", "),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )


                // -- SUBCATEGORIES --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.subcategories),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Shop subcategories",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )

                }

                if(state!!.subcategories!!.isEmpty())
                {
                    Text(
                        text = "-",
                        modifier = Modifier.padding(top = 8.dp),
                        style =MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
                else{
                    Text(
                        text = state!!.subcategories!!.joinToString(", "),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }


                // -- PICK UP TIME --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Pick up time",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }

                val workingHours = shopViewModel.state.value.shop!!.workingHours

                if (workingHours != null) {
                    for (workingHour in workingHours) {
                        val dayOfWeek = when (workingHour.day) {
                            1 -> "Mon"
                            2 -> "Tue"
                            3 -> "Wen"
                            4 -> "Tru"
                            5 -> "Fri"
                            6 -> "Sut"
                            7 -> "Sun"
                            else -> {"-"}
                        }
                        val openingHours = workingHour.openingHours
                        val closingHours = workingHour.closingHours
                        Text(
                        text = "$dayOfWeek: $openingHours - $closingHours",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }

                // -- REVIEWS --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Search icon",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Reviews",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }
                Text(
                    text = "View all reviews",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Text(
                    text = "Leave a review",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfilePic(shopViewModel: OneShopViewModel,id: Int) {
    val state = shopViewModel.state.value
    Box(
        modifier = Modifier
            .padding(top = 50.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        if(!shopViewModel.state.value.shop!!.isOwner)
        {
            ToggleImageButtonFunction(modifier = Modifier.align(Alignment.TopEnd), onClick={
                shopViewModel.changeToggleLike(id, shopViewModel.state.value.shop!!.id)
            })
        }
        else{
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Search icon",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clickable {

                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }


        Image(
            painter = painterResource(id = if(shopViewModel.state.value.shop!!.isOwner) R.drawable.addshop else R.drawable.navbar_message),
            contentDescription = "Search icon",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {

                },
        )

        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = "Search icon",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {

                },
            tint = MaterialTheme.colorScheme.primary
        )

        if(shopViewModel.state.value.shop!!.image == null)
        {
            Image(
                painter = painterResource(id = R.drawable.imageplaceholder),
                contentDescription = "Placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )
        }
        else
        {
            val image = shopViewModel.state.value.shop!!.image
            val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${image}"

            val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
            Image(
                painter = painter,
                contentDescription = "Placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )
        }
    }
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.shop!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = state.shop!!.address, style = MaterialTheme.typography.titleSmall,color = MaterialTheme.colorScheme.primary)
            RatingBar(rating = shopViewModel.state.value.shop!!.rating!!.toFloat())
        }
    }
}


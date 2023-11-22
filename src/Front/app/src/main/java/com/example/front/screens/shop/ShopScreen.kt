package com.example.front.screens.shop

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.SearchTextField
import com.example.front.components.ShopProductCard
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.ToggleImageButton
import com.example.front.screens.categories.ClickableCard
import kotlinx.coroutines.delay

@Preview
@Composable
fun ShopScreen() {
    var selectedColumnIndex by remember { mutableStateOf(true) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    )
    {
        item{
            SmallElipseAndTitle(title = "Shop")
        }
        item{
            //shop for user
            ProfilePic()
        }
        item{
            ShopInfo()
        }
    }
}


@Composable
fun ShopInfo() {
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

                Info(isImageClicked)
                Products(isImageClicked)

            }
        }
    }
}

@Composable
fun Products(isImageClicked: Boolean) {
    var showElseText by remember { mutableStateOf(false) }
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
        Column() {
            Row(
                modifier = Modifier.padding(top=50.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                SearchTextField(valuee = value, placeh = "Search products", onValueChangee = {}, modifier = Modifier.fillMaxWidth(0.75f))
                Image(
                    painter = painterResource(id = R.drawable.filters),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                )
            }
            LazyVerticalGrid (columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 25.dp)
                    .heightIn(400.dp, 600.dp)
            ) {
                items(data) { card ->
                    ShopProductCard(R.drawable.imageplaceholder, "Card 3", "120 din/kom" ,onClick={})
                }
            }
        }
    }
}

@Composable
fun Info(isImageClicked: Boolean) {
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
                    text = "Food",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )

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
                Text(
                    text = "Fruit, Vegetables",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )

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
                Text(
                    text = "Mon - Fri: 9:00 - 18:00",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Text(
                    text = "Avalska 78, Kragujevac, Serbia",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )
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
fun ProfilePic() {
    Box(
        modifier = Modifier
            .padding(top = 50.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        ToggleImageButton(modifier = Modifier.align(Alignment.TopEnd))
        Image(
            painter = painterResource(id = R.drawable.imageplaceholder),
            contentDescription = "Placeholder",
            contentScale = ContentScale.Crop,
            modifier = Modifier//.fillMaxWidth(0.3f)
                .size(140.dp)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape)
        )
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
            Text(text = "ELBrkito Shop", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "Bagremar, Kragujevac", style = MaterialTheme.typography.titleSmall,color = MaterialTheme.colorScheme.primary)
            RatingBar(rating = 3.5f)
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .fillMaxWidth(0.8f)
            )
            {
                ButtonWithIcon(text = "Message owner", onClick = { /*TODO*/ }, width = 0.48f, color =  MaterialTheme.colorScheme.primary,
                    imagePainter = painterResource(id = R.drawable.navbar_message), modifier = Modifier.padding(end=10.dp))
                Spacer(modifier = Modifier.width(10.dp))
                ButtonWithIcon(text = "Report shop", onClick = { /*TODO*/ }, width = 0.92f, color =  MaterialTheme.colorScheme.primary,
                    imagePainter = painterResource(id = R.drawable.navbar_bell))
            }
        }
    }
}


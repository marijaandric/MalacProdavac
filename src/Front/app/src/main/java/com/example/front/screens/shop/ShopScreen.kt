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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.Tabs
import com.example.front.components.ToggleImageButton
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
//        item{
//            Tabs(
//                onShopsSelected = { selectedColumnIndex = true },
//                onFavoritesSelected = { selectedColumnIndex = false },
//                selectedColumnIndex = selectedColumnIndex,
//                "Info",
//                "Products",
//                false
//            )
//        }
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
                    .padding(top = 60.dp)
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
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onBackground)
                    )
                }
                Text(
                    text = "Food",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.background)
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
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onBackground)
                    )
                }
                Text(
                    text = "Fruit, Vegetables",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.background)
                )
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


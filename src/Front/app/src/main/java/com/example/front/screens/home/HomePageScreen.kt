package com.example.front.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.ProductCard
import com.example.front.components.SearchTextField
import com.example.front.components.SellerCard

@Preview
@Composable
fun HomePage() {
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Search()
        }
        item {
            Sellers()
        }
        item {
            Products()
        }
    }


}

data class CardData(
    val title: String,
    val description: String,
    val imageResource: Int
)

@Composable
fun Products() {
    val products = listOf(
        CardData(
            title = "Vocnjak Brkit0",
            description = "Adresa 1",
            imageResource = R.drawable.jabuke
        ),
        CardData(
            title = "Radionica Onjo",
            description = "Adresa 2",
            imageResource = R.drawable.jabuke
        ),
        CardData(
            title = "Comine drangulije",
            description = "Adresa 3",
            imageResource = R.drawable.jabuke
        )
    )
    Column (
        modifier = Modifier
            .padding(16.dp, end = 0.dp, top = 20.dp)
    ){
        Text(text = "Recommended products", modifier = Modifier.padding(bottom = 10.dp))
        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 600.dp)
        ){
            items(products) { cardData ->
                ProductCard(
                    title = cardData.title,
                    price = cardData.description,
                    imageResource = cardData.imageResource,
                )
            }
        }
    }
}

@Composable
fun Sellers() {
    val sellers = listOf(
        CardData(
            title = "Vocnjak Brkit0",
            description = "Adresa 1",
            imageResource = R.drawable.jabuke
        ),
        CardData(
            title = "Radionica Onjo",
            description = "Adresa 2",
            imageResource = R.drawable.jabuke
        ),
        CardData(
            title = "Comine drangulije",
            description = "Adresa 3",
            imageResource = R.drawable.jabuke
        )
    )
    Column (
        modifier = Modifier
            .padding(16.dp, end = 0.dp)
    ){
        Text(text = "Recommended sellers", modifier = Modifier.padding(bottom = 10.dp))
        LazyRow(
            modifier = Modifier.heightIn(100.dp, 600.dp)
        ) {
            items(sellers) { cardData ->
                SellerCard(
                    title = cardData.title,
                    author = cardData.description,
                    imageResource = cardData.imageResource,
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search() {
    var value by remember {mutableStateOf("") }

    Box(
        contentAlignment = Alignment.TopCenter
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
                modifier = Modifier.padding(top = 35.dp)
            )
            {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Search icon",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(50.dp)
                        .align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.background
                )
                SearchTextField(valuee = value, placeh = "Search products and sellers", onValueChangee = { value = it })
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













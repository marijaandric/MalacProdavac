package com.example.front.screens.cart

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ProductCard
import com.example.front.components.TitleTextComponent
import com.example.front.screens.home.CardData
import com.example.front.viewmodels.cart.CartViewModel

@Composable
fun Cart(
    viewModel: CartViewModel,
    navController: NavHostController
) {
    LaunchedEffect(key1 = true) {
        viewModel.getCartProducts()
    }
    val cartState = viewModel.state.value

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.elipsemala),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    contentScale = ContentScale.FillWidth,


                )
                TitleTextComponent("Cart")
            }
//            Spacer(Modifier.height(200.dp))

//            if (cartState.isLoading) {
//                // Prikazati indikator ucitavanja ako podaci jos nisu usitani
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//            }

            LazyColumn {
                // Grupisanje proizvoda po prodavnicama
                val groupedProducts = cartState.products.groupBy { it.shopName }

                groupedProducts.forEach { (shopName, products) ->
                    // Header red za prikaz imena prodavnice
                    item {
                        Text(
                            text = shopName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    // Prikaz svakog proizvoda iz trenutne prodavnice
                    items(products) { product ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
        //                    Image()
                            Column {
                                Text(product.name)
                                Text(product.shopName)
                                Text(product.quantity.toString())
                                Text(product.price.toString())
                            }
                        } // Implementacija ProductCard-a zavisi od vašeg dizajna
                    }

                    // Spacer između grupa proizvoda
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    Surface() {
        //za donji deo sa CHECKOUT DUGMETOM
    }
}
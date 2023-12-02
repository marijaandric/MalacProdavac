package com.example.front.screens.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.checkout.CheckoutViewModel

@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    navController: NavHostController
){


    LaunchedEffect(key1 = true) {
        viewModel.getCheckoutData()
    }

    val checkoutState = viewModel.state.value

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
                ,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.elipsemala),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    contentScale = ContentScale.FillWidth,


                    )
                Text(
                    text = "Checkout",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .offset(y = 40.dp),
                    style = Typography.titleLarge,
                    color = Color.White
                )
            }

            //Spisak prodavnica
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .weight(1f)
            ) {
                checkoutState.forEach { shop ->
                    item {
                        Text(
                            text = shop.toString()
                        )
                    }
                }
            }
        }
    }
}


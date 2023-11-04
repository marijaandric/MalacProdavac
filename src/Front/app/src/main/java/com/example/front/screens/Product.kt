package com.example.front.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.ProductImage

@Composable
fun ProductPage(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box() {
            ProductImage(painterResource(
                id = R.drawable.jabukeproduct)
            )
            Image(
                painter = painterResource(id = R.drawable.srce),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
                    .align(Alignment.TopEnd)
            )
            Image(
                painter = painterResource(id = R.drawable.backarrow),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
                    .align(Alignment.TopStart)
            )
        }
    }
}

@Preview
@Composable
fun pregled(){
    ProductPage()
}
package com.example.front.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.ProductImage
import com.example.front.components.ToggleImageButton

@Composable
fun ProductPage(){
    Box() {
        ProductImage(painterResource(
            id = R.drawable.jabukeproduct)
        )
        ToggleImageButton(modifier = Modifier
                .align(Alignment.TopEnd))
        Image(
            painter = painterResource(id = R.drawable.backarrow),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp)
                .align(Alignment.TopStart)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 350.dp)
                .background(Color.White)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .border(1.dp,Color.Black)
        ) {
            
        }
    }
}

@Preview
@Composable
fun pregled(){
    ProductPage()
}
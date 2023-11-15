package com.example.front.screens.sellers

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.front.R
import com.example.front.components.SearchTextField
import com.example.front.components.SmallElipseAndTitle
import java.io.File

@Preview
@Composable
fun SellersScreen() {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        SmallElipseAndTitle("Sellers")
        SearchAndFilters()
        Mapa()
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Mapa() {
    
}


@Composable
fun SearchAndFilters() {
    var value by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    )
    {
        //SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it })
        SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it }, modifier = Modifier)
        Image(
            // ako budemo imali dark i light ovde mozda neki if i promena slike
            painter = painterResource(id = R.drawable.filters),
            contentDescription = "Search icon",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(40.dp)
                .align(Alignment.CenterVertically),
        )
    }
}



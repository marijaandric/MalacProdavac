package com.example.front.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.SearchTextField
import com.example.front.helper.TokenManager

@Preview
@Composable
fun HomePage() {
    Search()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search() {
    var value by remember {mutableStateOf("") }
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.elipse),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.padding(top = 35.dp, start=15.dp)
        )
        {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Search icon",
                modifier = Modifier
                    .padding(end=10.dp)
                    .size(50.dp)
                    .align(Alignment.CenterVertically),
                tint = MaterialTheme.colorScheme.background
            )
            SearchTextField(valuee = value, placeh = "Search products and sellers", onValueChangee = { value = it })
        }
        Text(text = tokenManager.getToken())
    }


}
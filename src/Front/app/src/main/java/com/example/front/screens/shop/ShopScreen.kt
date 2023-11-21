package com.example.front.screens.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.SmallElipseAndTitle

@Preview
@Composable
fun ShopScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color=MaterialTheme.colorScheme.background)
    )
    {
        item{
            SmallElipseAndTitle(title = "Shop")
        }
        item{
            //shop for user
            ProfilePic()
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
        Image(
            painter = painterResource(id = R.drawable.srce),
            contentDescription = "Placeholder",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
        )
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
                modifier = Modifier.padding(top=16.dp)
                    .fillMaxWidth(0.8f)
            )
            {
                Card(
                    modifier = Modifier.padding(end=10.dp)
                        .fillMaxWidth(0.5f)
                        .background(color=MaterialTheme.colorScheme.background)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground , shape = RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha=0.5f),
                        contentColor = MaterialTheme.colorScheme.tertiary.copy(alpha=0.5f)
                    )
                )
                {
                    Text("Message owner", modifier=Modifier.padding(8.dp).align(Alignment.CenterHorizontally), style=MaterialTheme.typography.displaySmall)
                }
                Card(
                    modifier = Modifier.padding()
                        .fillMaxWidth(0.95f)
                        .background(color=MaterialTheme.colorScheme.background)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground , shape = RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha=0.5f),
                        contentColor = MaterialTheme.colorScheme.tertiary.copy(alpha=0.5f)
                    )
                )
                {
                    Text("Report shop", modifier=Modifier.padding(8.dp).align(Alignment.CenterHorizontally), style=MaterialTheme.typography.displaySmall)
                }
            }
        }
    }
}

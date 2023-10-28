package com.example.front

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun RegistrationCategories() {
    //Title()
    Cards()
}

@Composable
fun Title() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.elipse),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Choose at least 2 categories:",
            fontSize = 30.sp,
            //style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 60.dp, start = 10.dp, end = 10.dp, bottom = 15.dp),
            lineHeight = 35.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.lexend)),
            color = Color.White
        )
    }
}

@Composable
fun Cards() {
    ClickableCard(R.drawable.elipse, "OPIS",
        onClick = {

        })
}

@Composable
fun ClickableCard(
    image: Int,
    description: String,
    onClick: () -> Unit
) {
    var isCardClicked by remember { mutableStateOf(false) }
    val cardColor = if (isCardClicked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    isCardClicked = !isCardClicked
                }
                .fillMaxWidth()
                .height(100.dp)
                .background(if (isCardClicked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(20)
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Food Icon",
                    modifier = Modifier.size(55.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 20.sp
        )
    }

}


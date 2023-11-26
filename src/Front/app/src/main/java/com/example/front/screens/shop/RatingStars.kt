package com.example.front.screens.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.front.R

@Composable
fun RatingBar(rating: Float) {
    if(rating != 0f)
    {
        val filledStars = rating.toInt()
        val remainingStar = 4 - filledStars
        val isHalfStar = rating - filledStars >= 0.5

        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
        ) {
            repeat(filledStars) {
                Image(
                    painter = painterResource(id = R.drawable.fullstar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 4.dp, top=8.dp)
                )
            }

            if (isHalfStar) {
                Image(
                    painter = painterResource(id = R.drawable.halfstar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 4.dp, top=8.dp)
                )
            }

            repeat(remainingStar) {
                Image(
                    painter = painterResource(id = R.drawable.emptystar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 4.dp, top=8.dp)
                )
            }
        }
    }
}




@Composable
fun StarRating(onClick: (Int) -> Unit) {
    var selectedStars by remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=16.dp, start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            val isFilled = index < selectedStars
            StarImage(isFilled) {
                selectedStars = index + 1
                onClick(selectedStars)
            }
        }
    }
}

@Composable
fun StarImage(isFilled: Boolean, onClick: () -> Unit) {
    val image = if (isFilled) R.drawable.fullstar else R.drawable.emptystar

    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
    )
}
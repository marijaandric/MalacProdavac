package com.example.front.screens.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.front.R

@Composable
fun RatingBar(rating: Float) {
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

package com.example.front.screens.userprofile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.front.R


@Preview
@Composable
fun UserProfileScreen() {
    Column {
        TopCenterImages()
        Info()
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Info() {
//    Box() {
//        var isImageClicked by remember { mutableStateOf(false) }
//
//        Box() {
//            Image(
//                painter = painterResource(id = R.drawable.infoelipse),
//                contentDescription = "Elipse",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//                    .zIndex(if (isImageClicked) 1f else 0f)
//                    .clickable {
//                        isImageClicked = !isImageClicked
//                    }
//            )
//            Image(
//                painter = painterResource(id = R.drawable.infoelipsedva),
//                contentDescription = "Elipse",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//                    .zIndex(if (isImageClicked) 0f else 1f)
//            )
//        }
//    }
    var isImageVisible by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isImageVisible, label = "ImageVisibilityTransition")

    val visibleAlpha by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            } else {
                tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            }
        },
        label = "VisibleAlpha"
    ) {
        1f//if (isImageVisible) 1f else 1f
    }

    val hiddenAlpha by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            } else {
                tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            }
        },
        label = "HiddenAlpha"
    ) {
        1f//if (isImageVisible) 1f else 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Slika koja se animira
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 0.0f),
                exit = fadeOut(targetAlpha = 0.0f),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.infoelipsedva),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(visibleAlpha)
                        .zIndex(if(isImageVisible) 1f else 0f)
                )
            }

            // Slika koja je inače ispred
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 1.0f),
                exit = fadeOut(targetAlpha = 1.0f),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.infoelipse),
                    contentDescription = "FrontImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(hiddenAlpha)
                        .zIndex(if(isImageVisible) 0f else 1f)
                )
                Text("Info", modifier = Modifier.clickable {
                    isImageVisible = !isImageVisible
                }, style=MaterialTheme.typography.h4)
            }
        }
    }

}

@Composable
fun TopCenterImages() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.elipse),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier.padding(top=140.dp),
                contentAlignment = Alignment.Center
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.jabuke),
                    contentDescription = "ProfilePic",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier//.fillMaxWidth(0.3f)
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                )
            }
        }
        Box(
            modifier = Modifier.padding(top=20.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Pryanka Chopra", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                Text(text = "Customer", style = MaterialTheme.typography.h6)
            }
        }
    }
}
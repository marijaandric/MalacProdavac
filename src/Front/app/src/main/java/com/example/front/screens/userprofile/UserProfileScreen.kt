package com.example.front.screens.userprofile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
//    var isImageClicked by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box {
//            Image(
//                painter = painterResource(id = R.drawable.infoelipse),
//                contentDescription = "Elipse",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//                    .zIndex(if (isImageClicked) 0.1f else 0f)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.infoelipsedva),
//                contentDescription = "Elipse",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//                    .zIndex(if (isImageClicked) 0f else 0.1f)
//            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .zIndex(2f),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    "Info",
//                    style = MaterialTheme.typography.h5,
//                    modifier = Modifier.clickable {
//                        isImageClicked = true
//                    }
//                )
//                Text(
//                    "Stats",
//                    style = MaterialTheme.typography.h5,
//                    modifier = Modifier.clickable {
//                        isImageClicked = false
//                    }
//                )
//            }
//        }
//    }
    var isImageClicked by remember { mutableStateOf(true) }

    val scaleImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.1.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val scaleImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 1.1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 0.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 0.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(top=20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.infoelipse),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
                    .zIndex(zIndexImage1.value)
                    .graphicsLayer(scaleX = scaleImage1.value, scaleY = scaleImage1.value)
            )
            Image(
                painter = painterResource(id = R.drawable.infoelipsedva),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
                    .zIndex(zIndexImage2.value)
                    .graphicsLayer(scaleX = scaleImage2.value, scaleY = scaleImage2.value)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp,top=40.dp, end = 16.dp)
                    .zIndex(2f)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(2f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Info",
                        style = MaterialTheme.typography.h5.copy(fontSize = if(isImageClicked) 30.sp else 25.sp),
                        modifier = Modifier.clickable {
                            isImageClicked = true
                        }
                    )
                    Text(
                        "Stats",
                        style = MaterialTheme.typography.h5.copy(fontSize = if(!isImageClicked) 30.sp else 25.sp),
                        modifier = Modifier.clickable {
                            isImageClicked = false
                        }
                    )
                }

                if(isImageClicked) {
                    AnimatedVisibility(
                        visible = isImageClicked,
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 60.dp)
                                .zIndex(2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Icon",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Username",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                            Text(
                                text = "marija.andric",
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.background)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Email Address",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                            Text(
                                text = "marijaandric2001@gmail.com",
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.background)
                            )
                        }
                    }
                }
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
package com.example.front

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TabRowDefaults.Indicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.front.ui.theme.FrontTheme
import kotlinx.coroutines.delay

class SplashScreen {
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash_screen")
        {
            composable("splash_screen")
            {
                SplashScreen(navController = navController)
            }
            composable("intro1")
            {
                Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
            }
        }
    }

    // -- Splash Screen --
    @Composable
    fun SplashScreen(navController:NavController) {
        val scale = remember {
            Animatable(2.5f)
        }
        LaunchedEffect(key1 = true) //side effect handlers - pogledati o ovome kasnije
        {
            scale.animateTo(
                targetValue = 2f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(3000L)
            navController.navigate("intro1")
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.scale(scale.value)
                )

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(70.dp)
                        .padding(8.dp),
                    strokeWidth = 4.dp
                )

            }
        }

    }

    // -- Intro Screen --
    @Composable
    fun Intro(
        title1: String,
        title2: String,
        imageString: String,
        currentPage:Int,
        navController: NavController
    ) {
        val scale = remember {
            Animatable(1.5f)
        }
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = title1,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 50.dp),
                lineHeight = 35.sp
            )

            Text(
                text = title2,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp),
                lineHeight = 35.sp
            )
            Spacer(modifier = Modifier.weight(1f))

            val context = LocalContext.current // Dobijanje trenutnog konteksta

            val imageResId = context.resources.getIdentifier(
                imageString, "drawable", context.packageName
            )

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Intro1",
                modifier = Modifier.scale(scale.value)
                    .padding(top = 8.dp, bottom = 10.dp)
            )

            val buttons = Buttons()
            buttons.MediumBlueButton(
                text = "Next",
                onClick = {

                }
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                repeat(4) { index ->
                    Indicator(selected = index == currentPage) // Postavite `selected` prema trenutnoj stranici
                }
            }



        }
    }



    // indikator da li su popunjene one tackice
    @Composable
    fun Indicator(selected: Boolean) {
        val indicatorSize = 8.dp
        val indicatorColor = if (selected) Color.Blue else Color.Gray

        Box(
            modifier = Modifier
                .size(indicatorSize)
                .background(indicatorColor, CircleShape)
        )
    }



}
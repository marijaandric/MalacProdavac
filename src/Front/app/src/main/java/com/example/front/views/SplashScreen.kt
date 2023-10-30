package com.example.front.views

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.front.components.MediumBlueButton
import com.example.front.R
import kotlinx.coroutines.delay

class SplashScreen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
                val desc = "Showcase your products and engage with local customers! Increase your visibility and sales."
                val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
                val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
                val pagerState = rememberPagerState(
                    initialPageOffsetFraction = 0f
                )
                Scaffold() {
                    HorizontalPager(pageCount = 4, state = pagerState) { page ->
                        val pageContent = when (page) {
                            0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                            1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                            2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                            3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                            else -> error("Invalid page: $page")
                        }
                        pageContent
                    }
                }
            }
            composable("intro2")
            {
                val desc = "Showcase your products and engage with local customers! Increase your visibility and sales."
                Intro("Business owners",desc,"intro2",1,navController = navController)
                val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
                val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
                val pagerState = rememberPagerState(
                    initialPage = 1,
                    initialPageOffsetFraction = 0f
                )

                Scaffold() {
                    HorizontalPager(pageCount = 4,state = pagerState) { page->
                        val pageContent = when (page) {
                            0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                            1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                            2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                            3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                            else -> error("Invalid page: $page")
                        }
                        pageContent
                    }
                }
            }
            composable("intro3")
            {
                val desc = "Explore a wide range of local products and services. Support your community."
                Intro("Customers",desc,"intro3",2,navController = navController)
                val desc2 = "Showcase your products and engage with local customers! Increase your visibility and sales."
                val desc3 = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
                val pagerState = rememberPagerState(
                    initialPage = 2,
                    initialPageOffsetFraction = 0f
                )
                Scaffold() {
                    HorizontalPager(pageCount = 4,state = pagerState) { page->
                        val pageContent = when (page) {
                            0 -> Intro("Welcome to MalacProdavac","","intro1",0,navController = navController)
                            1 -> Intro("Business owners", desc,"intro2", 1, navController = navController)
                            2 -> Intro("Customers",desc2,"intro3",2,navController = navController)
                            3 -> Intro("Delivery people",desc3,"intro4",3,navController = navController)
                            else -> error("Invalid page: $page")
                        }
                        pageContent
                    }
                }
            }
            composable("intro4")
            {
                val desc = "Efficiently manage your delivery schedule. Connect with local businesses and earn more."
                Intro("Delivery people",desc,"intro4",3,navController = navController)
                // Dodati HorizontalPager za login
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
            delay(2500L)
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
            modifier = Modifier
                .fillMaxSize()
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
                modifier = Modifier.padding(top = 60.dp, start = 10.dp, end = 10.dp, bottom = 15.dp),
                lineHeight = 35.sp,
                fontFamily = FontFamily(Font(R.font.lexend))
            )

            Text(
                text = title2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp, start = 10.dp, end = 10.dp),
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.lexend))
            )
            Spacer(modifier = Modifier.weight(4.5f))

            val context = LocalContext.current

            val imageResId = context.resources.getIdentifier(
                imageString, "drawable", context.packageName
            )

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Intro1",
                modifier = Modifier
                    .scale(scale.value)
                    .padding(top = 8.dp, bottom = 10.dp)
                    //.size(200.dp, 200.dp)
            )

            Spacer(modifier = Modifier.weight(1.5f))
            if(currentPage != 3)
            {
                Row()
                {
                    MediumBlueButton(
                        text = "Skip",
                        onClick = {
                            //navigacija ka login strani
                        },0.45f
                        , modifier = Modifier
                    )

                    MediumBlueButton(
                        text = "Next",
                        onClick = {
                            if(currentPage == 0)
                            {
                                navController.navigate("intro2")
                            }
                            if(currentPage == 1)
                            {
                                navController.navigate("intro3")
                            }
                            if(currentPage == 2)
                            {
                                navController.navigate("intro4")
                            }
                        },0.8f
                        , modifier =  Modifier
                    )
                }
            }
            else{
                Spacer(modifier = Modifier.weight(0.3f))
                MediumBlueButton(
                    text = "Get started",
                    onClick = {
                        //navigacija ka login strani
                    },0.8f
                    , modifier =  Modifier
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                repeat(4) { index ->
                    Indicator(selected = index == currentPage)
                }
            }

        }
    }



    // indikator da li su popunjene one tackice
    @Composable
    fun Indicator(selected: Boolean) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val lightColor = MaterialTheme.colorScheme.tertiary
        val indicatorSize = 8.dp
        val indicatorColor = if (selected) primaryColor else lightColor

        Box(
            modifier = Modifier
                .size(indicatorSize)
                .background(indicatorColor, CircleShape)
        )
    }

}
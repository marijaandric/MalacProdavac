package com.example.front.screens.userprofile

import android.annotation.SuppressLint
import android.graphics.Color.alpha
import android.util.Log
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.CardButton
import com.example.front.components.ImageItemForProfilePic
import com.example.front.components.MyTextField
import com.example.front.viewmodels.myprofile.MyProfileViewModel
import kotlinx.coroutines.delay


@Composable
fun UserProfileScreen(navController: NavHostController, myProfileViewModel: MyProfileViewModel) {
    val id = 1;


    LaunchedEffect(Unit) {
        myProfileViewModel.getMyProfileInfo(id)
    }
    if(myProfileViewModel.state.value.isLoading)
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            CircularProgressIndicator()
        }
    }
    else{
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            TopCenterImages(myProfileViewModel)
            Info(myProfileViewModel)
        }
    }

}

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Info(myProfileViewModel: MyProfileViewModel) {
    var isImageClicked by remember { mutableStateOf(true) }
    var firstTime by remember { mutableStateOf(true) }

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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.infoelipse),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage1.value)
                    .graphicsLayer(scaleX = scaleImage1.value, scaleY = scaleImage1.value)
            )
            Image(
                painter = painterResource(id = R.drawable.infoelipsedva),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage2.value)
                    .graphicsLayer(scaleX = scaleImage2.value, scaleY = scaleImage2.value)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 40.dp, end = 16.dp)
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
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(isImageClicked) 30.sp else 25.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = true
                        }
                    )
                    Text(
                        "Stats",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(!isImageClicked) 30.sp else 25.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = false
                        }
                    )
                }


                // -- INFO O KORISIKU --
                if(isImageClicked && myProfileViewModel.state.value.isLoading==false) {
                    var showText by remember { mutableStateOf(false) }

                    LaunchedEffect(isImageClicked) {
                        delay(200)
                        showText = true
                        firstTime = false
                    }
                    if(showText || firstTime) {

                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp)
                                .zIndex(2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Icon",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "Username",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onBackground)
                                )
                            }
                            myProfileViewModel.state.value.info?.let {
                                Text(
                                    text = it.username,
                                    modifier = Modifier.padding(top = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.background)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "Email Address",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onBackground)
                                )
                            }
                            Text(
                                text = myProfileViewModel.state.value.info!!.email,
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.background)
                            )
                        }
                    }
                }
                else if(myProfileViewModel.state.value.isLoading==false){
                    // -- STATS O KORISNIKU --
                    var showElseText by remember { mutableStateOf(false) }

                    LaunchedEffect(isImageClicked) {
                        delay(200)
                        showElseText = true
                    }
                    if(showElseText) {

                        Column(
                            modifier = Modifier
                                .padding(top = 60.dp)
                                .zIndex(2f)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = "Overall Rating",
                                        modifier = Modifier.padding(start = 8.dp),
                                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                                    )
                                }
                                Text(
                                    text = myProfileViewModel.state.value.info!!.rating.average.toString(),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Communication",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp,color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = myProfileViewModel.state.value.info!!.rating.communication.toString(),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                )
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Reliability",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp,color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = myProfileViewModel.state.value.info!!.rating.reliability.toString(),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Overall Experience",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp,color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = myProfileViewModel.state.value.info!!.rating.overallExperience.toString(),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                )
                            }

                            Column(
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info Icon",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = "Money spent",
                                        modifier = Modifier.padding(start = 8.dp),
                                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                                    )
                                }
                                Text(
                                    text = myProfileViewModel.state.value.info!!.moneySpent.toString()+ " din",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                )
                            }

                            Column(
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Date Icon",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = "Money earned",
                                        modifier = Modifier.padding(start = 8.dp),
                                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                                    )
                                }
                                Text(
                                    text = myProfileViewModel.state.value.info!!.moneyEarned.toString() + " din",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopCenterImages(myProfileViewModel: MyProfileViewModel) {
    val showDialog = remember { mutableStateOf(false) }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Search icon",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(50.dp)
                        .align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.background
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit icon",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if(myProfileViewModel.state.value.isLoading == false)
                            {
                                showDialog.value = true
                            }
                        },
                    tint = MaterialTheme.colorScheme.background
                )
            }

            Box(
                modifier = Modifier.padding(top=140.dp),
                contentAlignment = Alignment.Center
            )
            {
                if(myProfileViewModel.state.value.isLoading)
                {
                    // promeniti da bude neki placeholder
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
                else{
                    if(myProfileViewModel.state.value.info!!.image.isNotEmpty())
                    {
                        ImageItemForProfilePic(image = myProfileViewModel.state.value.info!!.image)
                    }
                }
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
                if(myProfileViewModel.state.value.isLoading == false)
                {
                    Text(text = myProfileViewModel.state.value.info!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = myProfileViewModel.state.value.info!!.role, style = MaterialTheme.typography.titleSmall,color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    if (showDialog.value) {
        EditDialog(onDismiss = { showDialog.value = false },myProfileViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(onDismiss: () -> Unit,myProfileViewModel: MyProfileViewModel) {
    val state = myProfileViewModel.state.value.info

    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

    var username by remember { mutableStateOf(state!!.username) }
    var address by remember { mutableStateOf(state!!.address) }
    var name by remember { mutableStateOf(state!!.name) }

    var checkbox1State by remember { mutableStateOf(state?.roleId == 2) }
    var checkbox2State by remember { mutableStateOf(state?.roleId == 3) }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onDismiss()
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Text("Edit profile", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 25.dp).align(Alignment.CenterHorizontally))

                    MyTextField(
                        labelValue = "Username",
                        painterResource = painterResource(id = R.drawable.user),
                        value = username,
                        onValueChange = { username = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MyTextField(
                        labelValue = "Name",
                        painterResource = painterResource(id = R.drawable.user),
                        value = name,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MyTextField(
                        labelValue = "Address",
                        painterResource = painterResource(id = R.drawable.user),
                        value = address,
                        onValueChange = { address = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Checkbox(
                            checked = checkbox1State,
                            onCheckedChange = { checked ->
                                checkbox1State = checked
                                if (checked) {
                                    checkbox2State = false
                                }
                            },
                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                checkedColor = Color(
                                    0xFF005F8B
                                )
                            ),
                            modifier = Modifier
                                .padding(0.dp)
                                .height(24.dp)
                        )
                        androidx.compose.material3.Text(
                            text = "Business Owner",
                            modifier = Modifier.padding(0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkbox2State,
                            onCheckedChange = { checked ->
                                checkbox2State = checked
                                if (checked) {
                                    checkbox1State = false
                                }
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF005F8B)),
                            modifier = Modifier
                                .padding(0.dp)
                                .height(24.dp)
                        )
                        androidx.compose.material3.Text(
                            text = "Delivery person",
                            modifier = Modifier.padding(0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    CardButton(text = "Save", onClick = { onDismiss() }, width = 0.5f, modifier = Modifier.align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.secondary)

                }

            }
        }
    }

}
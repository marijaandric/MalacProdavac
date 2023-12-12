package com.example.front.screens.myshop

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.CardButton
import com.example.front.components.MyTextField
import com.example.front.components.MyTextFieldWithoutIcon
import com.example.front.components.OpenNow
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.screens.sellers.FilterCard
import com.example.front.screens.sellers.ReviewStars
import com.example.front.viewmodels.myshop.MyShopViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpShopScreen(navController : NavHostController, viewModel: MyShopViewModel) {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        viewModel.dataStoreManager
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        )
        {
            item {
                SmallElipseAndTitle(title = "Shop Setup", drawerState)
            }
            item {
                ProfilePhoto(context)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePhoto(context:Context) {
    var uri by remember {
        mutableStateOf<Uri?>(null)
    }
    var im by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    val state = rememberTimePickerState()

    var photoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(), onResult = {
        uri?.let {

        }
    })

    val cardData = listOf(
        "Food", "Drink", "Footwear", "Clothes", "Jewerly", "Tools", "Furniture", "Pottery", "Beauty", "Health", "Decor", "Other"
    )

    var brojevi = (1..12).map { it }
    var kombinovanaLista = cardData.zip(brojevi)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // slika
        Box(
            modifier = Modifier
                .size(165.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Upload shop profile photo",
                    modifier = Modifier.padding(5.dp,top = 8.dp),
                    style = MaterialTheme.typography.displaySmall.copy(color=MaterialTheme.colorScheme.background),
                    textAlign = TextAlign.Center
                )

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        MyTextFieldWithoutIcon(labelValue = "Shop Name", value = name, onValueChange={ name=it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        MyTextFieldWithoutIcon(labelValue = "Shop Address", value = address, onValueChange={ address=it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Shop Categories", modifier = Modifier.padding(top = 16.dp,bottom = 10.dp, start = 10.dp), style=MaterialTheme.typography.displaySmall)
        LazyVerticalGrid(
            modifier = Modifier.heightIn(30.dp,500.dp),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(kombinovanaLista,key={(_, number) -> number }) { (cardText,number) ->
                FilterCard(cardText = cardText, onClick = {
                }, false)
            }
        }
        Text(text = "Pick up time", modifier = Modifier.padding(top = 16.dp,start = 10.dp), style=MaterialTheme.typography.displaySmall)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
            for (day in daysOfWeek) {
                DayOfWeekItem(day = day, onClick={})
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .padding(10.dp)
        )
        {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(text="Pick up time", style=MaterialTheme.typography.bodyLarge)
                Text(text = "Opening time", modifier = Modifier.padding(top = 16.dp,start = 10.dp, bottom = 10.dp), style=MaterialTheme.typography.displaySmall)
                TimeInput(state = state)
                Text(text = "Closing time", modifier = Modifier.padding(top = 16.dp,start = 10.dp, bottom = 10.dp), style=MaterialTheme.typography.displaySmall)
                TimeInput(state = state)
                CardButton(text = "Apply", onClick = {  }, width = 0.7f, modifier = Modifier, color = MaterialTheme.colorScheme.secondary)
            }
        }
        BigBlueButton(text = "Proceed", onClick = {  }, width = 0.9f, modifier = Modifier)

    }
}

@Composable
fun DayOfWeekItem(day: String, onClick: () -> Unit ) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .height(48.dp)
            .width(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
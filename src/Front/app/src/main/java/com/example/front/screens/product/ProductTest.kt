package com.example.front.screens.product

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.GalleryComponent
import com.example.front.components.ProductImage
import com.example.front.components.ToggleImageButton
import com.example.front.model.DTO.ImageDataDTO
import com.example.front.model.DTO.WorkingHoursDTO
import com.example.front.navigation.Screen
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.product.ProductViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun producttest() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        content = {
                Box() {
                    ProductImage(
                        painterResource(
                            id = R.drawable.jabukeproduct
                        )
                    )
                    ToggleImageButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.backarrow),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .align(Alignment.TopStart)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .border(1.dp, Color.Black)
                            .offset(y = 300.dp)
                    ) {
                            GalleryComponent(
                                images = listOf(
                                   ImageDataDTO(id = 1, image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAWElEQVR42mP8/wcfBhAphA2FRUMAAAAASUVORK5CYII="),
                                    ImageDataDTO(id = 2, image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAWElEQVR42mP8/wcfBhAphA2FRUMAAAAASUVORK5CYII="),
                                    ImageDataDTO(id = 3, image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAWElEQVR42mP8/wcfBhAphA2FRUMAAAAASUVORK5CYII=")),
                                modifier = Modifier.padding(20.dp)
                            )
                            Text(
                                text = "Jabuka",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                style = Typography.titleMedium,
                            )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                                Text(
                                    text = "Maja prodavnica",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    style = Typography.titleSmall,
                                    textAlign = TextAlign.Center
                                )
                            Image(
                                painter = painterResource(R.drawable.strelica),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(29.dp)
                                    .align(Alignment.Center)
                                    .offset(x = 90.dp)
                            )
                        }
                        Box() {
                            Text(
                                text = "50" + " rsd",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                style = Typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                        Box() {
                                Text(
                                    text = "Popust 10% na vise od 10kg",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    style = Typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                        }
                        Box() {
                                Text(
                                    text = "Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsumLorem ipsum",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    style = Typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 16.dp),
                            color = Color.Gray
                        )
                        //productInfo?.workingHours?.let { ExpandableRow1(it) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Quantity",
                                style = Typography.titleSmall,
                            )
                            Row() {
                                Button(
                                    onClick = {},
                                    modifier = Modifier
                                        .height(40.dp)
                                        .border(1.dp, Color.Black),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                ) {
                                    Text(
                                        text = "-",
                                        style = Typography.bodyLarge
                                    )
                                }
                                Text(
                                    text = "1",
                                    modifier = Modifier
                                        .padding(20.dp),
                                    textAlign = TextAlign.Justify
                                )
                                Button(
                                    onClick = {},
                                    modifier = Modifier
                                        .height(40.dp)
                                        .border(1.dp, Color.Black),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                ) {
                                    Text(
                                        text = "+",
                                        style = Typography.bodyLarge,
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(200.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xE48359)),
                            ) {
                                Text(
                                    text = "Add To Cart",
                                )
                            }
                        }
                    }
                }
        }
    )
}

@Composable
fun ExpandableRow1(workingHoursList: List<WorkingHoursDTO>) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pick up time",
                style = Typography.titleSmall,
            )

            Text(
                text = "Mon - Fri   ${getDayName1(workingHoursList.firstOrNull()?.day ?: 0)}   " +
                        "${workingHoursList.firstOrNull()?.openingHours} - ${workingHoursList.firstOrNull()?.closingHours}",
                style = Typography.titleSmall,
            )
        }

        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                workingHoursList.forEach { workingHours ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pick up time",
                            style = Typography.titleSmall,
                        )

                        Text(
                            text = "${getDayName1(workingHours.day)}   " +
                                    "${workingHours.openingHours} - ${workingHours.closingHours}",
                            style = Typography.titleSmall,
                        )
                    }
                }
            }
        }
    }
}

fun getDayName1(day: Int): String {
    return when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        else -> "Unknown Day"
    }
}

@Preview
@Composable
fun test(){
    producttest()
}

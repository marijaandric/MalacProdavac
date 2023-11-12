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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.GalleryComponent
import com.example.front.components.ProductImage
import com.example.front.components.ToggleImageButton
import com.example.front.model.DTO.WorkingHoursDTO
import com.example.front.navigation.Screen
import com.example.front.ui.theme.Black
import com.example.front.ui.theme.DarkBlue
import com.example.front.ui.theme.Orange
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.product.ProductViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProductPage(navHostController: NavHostController, productViewModel: ProductViewModel) {

    LaunchedEffect(Unit) {
        productViewModel.getProductInfo(1, 1)
    }

    val productInfo = productViewModel.state.value.product

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (productViewModel.state.value.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                color = Color.Magenta
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
            ) {
                ProductImage(painterResource(id = R.drawable.jabukeproduct))

                ToggleImageButton(modifier = Modifier.align(Alignment.TopEnd))

                Image(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                        .align(Alignment.TopStart)
                        .clickable { navHostController.navigate(Screen.Home.route) }
                )
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .weight(2f)
                    .verticalScroll(rememberScrollState())
            ) {
                productInfo?.images?.let {
                    GalleryComponent(images = it, modifier = Modifier.padding(20.dp))
                }

                productInfo?.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        style = Typography.titleMedium,
                        color = DarkBlue
                    )
                }

                productInfo?.shopName?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }

                Image(
                    painter = painterResource(R.drawable.strelica),
                    contentDescription = "",
                    modifier = Modifier
                        .size(29.dp)
                        .offset(x = 90.dp)
                )

                Text(
                    text = "${productInfo?.price} rsd",
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    style = Typography.titleLarge,
                    color = DarkBlue,
                    textAlign = TextAlign.Center
                )

                productInfo?.saleMessage?.let {
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        style = Typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = Orange
                    )
                }

                productInfo?.description?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        style = Typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp),
                    color = Color.Gray
                )

                productInfo?.workingHours?.let { ExpandableRow(it) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Quantity",
                        style = Typography.titleSmall,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .height(40.dp)
                                .border(1.dp, Color.Black),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        ) {
                            Text(text = "-")
                        }

                        Text(
                            text = "1",
                            modifier = Modifier.padding(20.dp),
                            textAlign = TextAlign.Justify
                        )

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .height(40.dp)
                                .border(1.dp, Color.Black),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        ) {
                            Text(text = "+")
                        }
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .height(60.dp)
                        .width(200.dp)
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xE48359)),
                ) {
                    Text(text = "Add To Cart", style = Typography.titleSmall)
                }
            }
        }

    }
}


@Composable
fun ExpandableRow(workingHoursList: List<WorkingHoursDTO>) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Click to see pick up times",
                style = Typography.titleSmall,
                color = Color.Black
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${getDayName(workingHours.day)}   " +
                                    "${workingHours.openingHours} - ${workingHours.closingHours}",
                            style = Typography.displaySmall,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

fun getDayName(day: Int): String {
    return when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        else -> "Unknown Day"
    }
}


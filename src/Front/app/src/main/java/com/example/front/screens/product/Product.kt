package com.example.front.screens.product

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
import com.example.front.model.product.WorkingHoursDTO
import com.example.front.navigation.Screen
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.product.ProductViewModel

@Composable
fun ProductPage(navHostController: NavHostController,productViewModel: ProductViewModel){


    LaunchedEffect(Unit)
    {
        productViewModel.getProductInfo(1,1)
    }
    val productInfo by productViewModel.productInfo.collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    content = {
//        if(productViewModel.state.value.isLoading)
//        {
//            CircularProgressIndicator()
//        }
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
                    .clickable { navHostController.navigate(Screen.Home.route) }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 300.dp)
                    .background(Color.White)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, Color.Black)
            ) {
                productInfo?.images?.let {
                    GalleryComponent(
                        images = it,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                productInfo?.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        style = Typography.titleMedium,
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    productInfo?.shopName?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            style = Typography.titleSmall,
                            textAlign = TextAlign.Center
                        )
                    }
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
                        text = productInfo?.price.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        style = Typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Box() {
                    productInfo?.saleMessage?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Box() {
                    productInfo?.description?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            style = Typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp) // Adjust the height as needed
                        .padding(horizontal = 16.dp), // Adjust the padding as needed
                    color = Color.Gray // You can set the color of the divider
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pick up time",
                style = Typography.titleSmall,
            )

            Text(
                text = "Mon - Fri   ${getDayName(workingHoursList.firstOrNull()?.day ?: 0)}   " +
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
                            text = "${getDayName(workingHours.day)}   " +
                                    "${workingHours.openingHours} - ${workingHours.closingHours}",
                            style = Typography.titleSmall,
                        )
                    }
                }
            }
        }
    }
}

// Function to get the day name based on day number
fun getDayName(day: Int): String {
    // Implement logic to get the day name based on your requirements
    // For simplicity, using a basic mapping here
    return when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        // Add more cases as needed
        else -> "Unknown Day"
    }
}


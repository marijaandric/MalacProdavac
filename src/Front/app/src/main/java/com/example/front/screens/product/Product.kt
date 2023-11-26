package com.example.front.screens.product

import android.annotation.SuppressLint
import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.GalleryComponent
import com.example.front.components.ProductImage
import com.example.front.components.ToggleImageButton
import com.example.front.model.DTO.WorkingHoursDTO
import com.example.front.model.product.ProductReviewUserInfo
import com.example.front.navigation.Screen
import com.example.front.ui.theme.DarkBlue
import com.example.front.ui.theme.Orange
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.product.ProductViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Integer.max

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProductPage(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    productID: Int
) {

    LaunchedEffect(Unit) {
        productViewModel.getUserId()?.let { productViewModel.getProductInfo(productID, it) }
        productViewModel.getReviewsForProduct(productID, 0)
    }

    DisposableEffect(productID) {
        onDispose {
            productViewModel.resetReviewState()
        }
    }

    val productInfo = productViewModel.state.value.product
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productViewModel.state.value.isLoading) {
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
        } else {
            var selectedImage by remember { mutableStateOf(productInfo?.images?.get(0)) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Transparent)
            ) {
                if (productInfo != null) {
                    selectedImage?.let { image ->
                        if (image.image.isNotEmpty()) {
                            ProductImage(image.image, modifier = Modifier)
                        }
                    }
                }

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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                    .background(Color.Transparent)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(
                            top = 20.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 0.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    productInfo?.images?.let { images ->
                        GalleryComponent(modifier = Modifier.padding(20.dp),images = images, selectedImage = selectedImage) { selectedImage = it }
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        productInfo?.shopName?.let {
                            Text(
                                text = it,
                                modifier = Modifier
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
                                .clickable {
                                    navHostController.navigate("${Screen.Shop.route}/${productInfo?.shopId}")
                                }
                        )
                    }

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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quantity",
                            style = Typography.titleSmall,
                            color = Color.Black
                        )

                        NumberPicker()
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .height(80.dp)
                                .width(300.dp)
                                .padding(20.dp),
                        ) {
                            Text(text = "Add To Cart", style = Typography.titleSmall)
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp),
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Product reviews", style = Typography.bodyLarge)
                            Row {
                                if (productInfo != null) {
                                    Text(
                                        text = "${productInfo.rating}",
                                        style = Typography.bodyLarge
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star icon"
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 16.dp),
                            color = Color.Gray
                        )
                        Row(modifier = Modifier.padding(10.dp)) {
                            val reviews = productViewModel.stateReview.value.reviews

                            if (reviews.isNullOrEmpty()) {
                                Text("No reviews available", style = Typography.bodySmall)
                            } else {
                                Column {
                                    ReviewCard(productReviewUserInfo = reviews[0])

                                    var reviewCounter by remember { mutableStateOf(1) }
                                    var showAllReviews by remember { mutableStateOf(false) }

                                    if (showAllReviews) {
                                        for (i in 1..reviewCounter) {
                                            ReviewCard(productReviewUserInfo = reviews[i - 1])
                                        }
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {

                                        Button(
                                            onClick = {
                                                showAllReviews = !showAllReviews

                                                if (showAllReviews) {
                                                    GlobalScope.launch {
                                                        productViewModel.getReviewsForProduct(
                                                            productID,
                                                            reviews.size
                                                        )
                                                    }
                                                }
                                            }
                                        ) {
                                            Text(if (showAllReviews) "Show less reviews" else "Show all reviews")
                                        }
                                    }

                                    DisposableEffect(reviewCounter) {
                                        if (showAllReviews) {
                                            GlobalScope.launch {
                                                productViewModel.getReviewsForProduct(
                                                    productID,
                                                    reviews.size
                                                )
                                            }
                                        }
                                        onDispose {
                                        }
                                    }
                                }
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun NumberPicker() {
    var value by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = {
                value = max(value - 1, 1)
            },
            modifier = Modifier
                .height(30.dp)
                .weight(0.7f)
                .border(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Text(text = "-", color = MaterialTheme.colorScheme.onSurface)
        }

        BasicTextField(
            value = value.toString(),
            onValueChange = {
                // Ensure the input is a valid integer and update the value accordingly
                value = it.toIntOrNull() ?: value
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center), // Center the text horizontally
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        )

        Button(
            onClick = {
                value += 1
            },
            modifier = Modifier
                .height(30.dp)
                .weight(0.7f)
                .border(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Text(text = "+", color = MaterialTheme.colorScheme.onSurface)
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

@Composable
fun ReviewCard(productReviewUserInfo: ProductReviewUserInfo) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(5.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Row() {
                    val imageUrl =
                        "http://softeng.pmf.kg.ac.rs:10015/images/${productReviewUserInfo.image}"

                    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${productReviewUserInfo.username}",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                    for (i in 1..productReviewUserInfo.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star icon"
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                    Text(text = "${productReviewUserInfo.comment}", style = Typography.bodySmall)
                }
            }
            Column {
                Row() {
                    Text(
                        text = "Posted ${productReviewUserInfo.getDaysSincePosted()} days ago",
                        style = Typography.bodySmall
                    )
                }
            }
        }
    }
}

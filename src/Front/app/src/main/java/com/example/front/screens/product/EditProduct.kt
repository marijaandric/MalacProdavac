package com.example.front.screens.product

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.front.R
import com.example.front.components.CommentsTextBox
import com.example.front.components.MyDropdownCategories
import com.example.front.components.MyDropdownMetrics
import com.example.front.components.MyTextFieldWithoutIcon
import com.example.front.model.DTO.CategoriesDTO
import com.example.front.model.DTO.MetricsDTO
import com.example.front.model.DTO.NewProductDTO
import com.example.front.model.product.ProductInfo
import com.example.front.screens.shop.contentOfAddNewImage
import com.example.front.viewmodels.oneshop.OneShopViewModel

@Composable
fun EditProduct(onDismiss: () -> Unit, shopViewModel: OneShopViewModel, productInfo: ProductInfo) {
    var currentStep by remember { mutableStateOf(0) }

    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

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
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(top = 5.dp)
                    .align(Alignment.Center),
            ) {
                when (currentStep) {
                    0 -> {
                        contentOfAddEditProduct(shopViewModel, productInfo ,onNextClicked = { currentStep = 1 })
                    }

                    1 -> {
                        contentOfAddNewImage(shopViewModel, onNextClicked = { currentStep = 0 })
                    }
                }
            }
        }
    }
}

@Composable
fun contentOfAddEditProduct(shopViewModel: OneShopViewModel, productInfo: ProductInfo ,onNextClicked: () -> Unit) {
    var productName by remember { mutableStateOf(productInfo.name) }
    var productDescription by remember { mutableStateOf(productInfo.description) }
    var selectedMetric by remember { mutableStateOf(MetricsDTO(productInfo.metricId!!,productInfo.metric!!)) }
    var selectedCategory by remember { mutableStateOf(CategoriesDTO(productInfo.categoryId!!,productInfo.category!!)) }
    var price by remember { mutableStateOf(productInfo.price) }
    var salePercentage by remember { mutableStateOf(productInfo.salePercentage) }
    var saleMinQuantity by remember { mutableStateOf(productInfo.saleMinQuantity) }
    var saleMessage by remember { mutableStateOf(productInfo.saleMessage) }
    var shopId by remember { mutableStateOf(productInfo.shopId) }
    var weight by remember { mutableStateOf(productInfo.mass) }


    Column {
        if (shopViewModel.stateGetCategories.value.isLoading && shopViewModel.stateGetMetrics.value.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Add new product",
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier
                        )
                    }
                }
                item {
                    Box(modifier = Modifier.padding(start = 16.dp, end = 10.dp)) {
                        MyTextFieldWithoutIcon(
                            labelValue = "Product name",
                            value = productName!!,
                            onValueChange = {
                                productName = it
                            },
                            modifier = Modifier
                        )
                    }
                }
                item {
                    CommentsTextBox(onReviewTextChanged = {}, "Product Description")
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        MyTextFieldWithoutIcon(
                            labelValue = "Unity price",
                            value = price.toString(),
                            onValueChange = {
                                price = it.toFloat()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 2.dp)
                        )
                        shopViewModel.stateGetMetrics.value.metrics?.let {
                            MyDropdownMetrics(
                                labelValue = "Metric",
                                selectedMetric = selectedMetric,
                                metricsList = it,
                                onMetricSelected = { newMetric ->
                                    selectedMetric = newMetric
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 2.dp)
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        MyTextFieldWithoutIcon(
                            labelValue = "Weight of single piece",
                            value = weight.toString(),
                            onValueChange = {
                                weight = it.toFloat()
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        Row() {
                            MyTextFieldWithoutIcon(
                                labelValue = "Discount",
                                value = salePercentage.toString(),
                                onValueChange = {
                                    salePercentage = it.toFloat()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 2.dp)
                            )
                            MyTextFieldWithoutIcon(
                                labelValue = "If quantity over ...",
                                value = saleMinQuantity.toString(),
                                onValueChange = {
                                    saleMinQuantity = it.toFloat()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 2.dp)
                            )
                        }
                        Row() {
                            MyTextFieldWithoutIcon(
                                labelValue = "Discount message",
                                value = saleMessage!!,
                                onValueChange = {
                                    saleMessage = it
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 2.dp)
                            )
                        }
                    }
                }
                item {
                    Row(modifier = Modifier.padding(start = 16.dp, end = 10.dp)) {
                        shopViewModel.stateGetCategories.value.categories?.let {
                            MyDropdownCategories(
                                labelValue = "Category",
                                selectedCategory = selectedCategory,
                                categoriesList = it,
                                onCategorySelected = { newCat ->
                                    selectedCategory = newCat
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        MyTextFieldWithoutIcon(
                            labelValue = "Price",
                            value = price.toString(),
                            onValueChange = {
                                price = it.toFloat()
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            val newProductDTO = NewProductDTO(
                                name = productName!!,
                                description = productDescription!!,
                                metricId = selectedMetric.id,
                                price = price!!,
                                salePercentage = salePercentage!!,
                                saleMinQuantity = saleMinQuantity!!,
                                saleMessage = saleMessage!!,
                                categoryId = selectedCategory.id,
                                shopId = shopId!!,
                                weight = weight!!
                            )
                            shopViewModel.postNewProduct(newProductDTO)
                            onNextClicked()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Proceed")
                    }
                }
            }
        }
    }
}

@Composable
fun contentOfAddNewImageOnEdit(shopViewModel: OneShopViewModel, onNextClicked: () -> Unit) {

    var selectedImageUris by remember { mutableStateOf(emptyList<Uri>()) }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUris = selectedImageUris + it
                Log.d("ImagePicker", "New image added: $it")
            }
        }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Add images for product",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.upload_image),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(20.dp)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        selectedImageUris.forEach { uri ->
                            shopViewModel.uploadImage(1, 17, uri)
                            Log.d("ImagePicker", "New image added: $uri")
                        }
                    }
                ) {
                    Text("Upload Images")
                }
            }
        }
    }
}
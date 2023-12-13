package com.example.front.components

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.model.DTO.CategoriesDTO
import com.example.front.model.DTO.ImageDataDTO
import com.example.front.model.DTO.MetricsDTO
import com.example.front.navigation.Screen
import com.example.front.ui.theme.LightBlue
import com.example.front.ui.theme.MainBlue
import com.example.front.ui.theme.Typography
import kotlinx.coroutines.launch
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun TitleTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = Typography.titleLarge,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    labelValue: String,
    painterResource: Painter?,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isLast: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp)),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = LightBlue,
            focusedLabelColor = LightBlue,
            cursorColor = LightBlue
        ),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            if (painterResource != null) {
                Icon(
                    painter = painterResource,
                    contentDescription = "",
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        shape = RoundedCornerShape(20.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MyDropdownCategories(
    labelValue: String,
    selectedCategory: CategoriesDTO?,
    categoriesList: List<CategoriesDTO>,
    onCategorySelected: (CategoriesDTO) -> Unit,
    modifier: Modifier
) {
    var currentSelectedCategory by remember { mutableStateOf<CategoriesDTO?>(selectedCategory) }
    var isPressed by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .pointerInteropFilter { event ->
                isPressed = event.action == MotionEvent.ACTION_DOWN
                false
            },
        readOnly = true,
        value = currentSelectedCategory?.name ?: "",
        onValueChange = {},
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            focusedLabelColor = Color.Blue,
            cursorColor = Color.Blue
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(20.dp)
    )

    DropdownMenu(
        expanded = isPressed,
        onDismissRequest = { isPressed = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        categoriesList.forEach { category ->
            DropdownMenuItem(onClick = {
                currentSelectedCategory = category
                onCategorySelected(category)
                isPressed = false
            }) {
                Text(text = category.name)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MyDropdownMetrics(
    labelValue: String,
    selectedMetric: MetricsDTO?,
    metricsList: List<MetricsDTO>,
    onMetricSelected: (MetricsDTO) -> Unit,
    modifier: Modifier
) {
    var currentSelectedCategory by remember { mutableStateOf<MetricsDTO?>(selectedMetric) }
    var isPressed by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .pointerInteropFilter { event ->
                isPressed = event.action == MotionEvent.ACTION_DOWN
                false
            },
        readOnly = true,
        value = currentSelectedCategory?.name ?: "",
        onValueChange = {},
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            focusedLabelColor = Color.Blue,
            cursorColor = Color.Blue
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(20.dp)
    )

    DropdownMenu(
        expanded = isPressed,
        onDismissRequest = { isPressed = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        metricsList.forEach { metric ->
            DropdownMenuItem(onClick = {
                currentSelectedCategory = metric
                onMetricSelected(metric)
                isPressed = false
            }) {
                Text(text = metric.name)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldWithoutIcon(
    labelValue: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    isLast: Boolean = false,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .then(modifier),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = LightBlue,
            focusedLabelColor = LightBlue,
            cursorColor = LightBlue
        ),
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        shape = RoundedCornerShape(20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CommentsTextBox(onReviewTextChanged: (String) -> Unit, placeholder: String) {
    var reviewText by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        OutlinedTextField(
            value = reviewText,
            onValueChange = {
                reviewText = it
                isButtonEnabled = it.isNotBlank()
                onReviewTextChanged(it)
            },
            textStyle = TextStyle.Default.copy(
                fontSize = 18.sp
            ),
            label = { Text(text = placeholder) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),//
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    //LocalSoftwareKeyboardController.current?.hide()
                }
            ),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(5.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    valuee: String,
    placeh: String,
    onValueChangee: (String) -> Unit,
    modifier: Modifier
) {
    var value = valuee

    OutlinedTextField(
        value = value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.onSurface,
            containerColor = Color.White
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        onValueChange = onValueChangee,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .height(50.dp)
            .then(modifier),
        placeholder = {
            Text(
                text = placeh,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}


@Composable
fun HeaderImage(painterResource: Painter) {
    Image(
        painter = painterResource,
        contentDescription = "",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ProductImage(image: String, modifier: Modifier) {
    val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${image}"

    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .then(modifier),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun ToggleImageButton(modifier: Modifier) {
    var isToggled by remember { mutableStateOf(false) }

    val currentImage = if (isToggled) painterResource(id = R.drawable.srcefull)
    else painterResource(id = R.drawable.srce)

    Image(
        painter = currentImage,
        contentDescription = "",
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
            .then(modifier)
            .clickable {
                isToggled = !isToggled
            }
    )
}

@Composable
fun ToggleImageButtonFunction(modifier: Modifier, onClick: () -> Unit, isLiked: Boolean) {
    var isToggled by remember { mutableStateOf(isLiked) }

    val currentImage = if (isToggled) painterResource(id = R.drawable.srcefull)
    else painterResource(id = R.drawable.srce)

    Image(
        painter = currentImage,
        contentDescription = "",
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
            .then(modifier)
            .clickable {
                isToggled = !isToggled
                onClick()
            }
    )
}


@Composable
fun LogoImage(painterResource: Painter, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource,
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .then(modifier)
    )
}


@Composable
fun ErrorTextComponent(text: String) {
    Text(
        text = text,
        color = Color(0xFFD50505),
        modifier = Modifier
            .padding(bottom = 4.dp, start = 6.dp),
        fontFamily = FontFamily(Font(R.font.lexend)),
        fontWeight = FontWeight(200)
    )
}

@Composable
fun MediumBlueButton(text: String, onClick: () -> Unit, width: Float, modifier: Modifier) {
    val primaryColor = MainBlue
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(8.dp)
            .height(48.dp)
            .then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        shape = RoundedCornerShape(20)
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge
        )
    }
}

@Composable
fun CardButton(text: String, onClick: () -> Unit, width: Float, modifier: Modifier, color: Color) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(30)
    ) {
        Text(
            text = text,
            style = Typography.labelSmall.copy(color = MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
fun OrderCard(orderid:String, quantity: Int, amount: Float, date: String, status: String, navController: NavHostController, id: Int) {
    Column (
        modifier = Modifier.padding(16.dp)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .height(140.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(orderid, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
                    Text(date, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Quantity: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
                    Text(quantity.toString(), style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Total Amount: ", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraLight, color=MaterialTheme.colorScheme.primary))
                    Text(amount.toString()+" RSD", style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    CardButton(text = "Details", onClick = {
                        navController.navigate("${Screen.Order.route}/$id")
                    }, width = 0.5f, modifier = Modifier, color = MaterialTheme.colorScheme.secondary)
                    Text(status, style=MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color=MaterialTheme.colorScheme.primary))
                }

            }
        }
    }
}

@Composable
fun ButtonWithIcon(
    text: String,
    onClick: () -> Unit,
    width: Float,
    modifier: Modifier = Modifier,
    color: Color,
    imagePainter: Painter? = null,
    imageModifier: Modifier = Modifier,
    height: Int?
) {
    var h = 32
    if (height != null) {
        h = height
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(width)
            .height(h.dp)
            .clip(RoundedCornerShape(30))
            .background(color)
            .then(modifier)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (imagePainter != null) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(23.dp)
                    .then(imageModifier)
                    .padding(end = 4.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
fun BigBlueButton(text: String, onClick: () -> Unit, width: Float, modifier: Modifier) {
    val primaryColor = MainBlue
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(8.dp)
            .height(58.dp)
            .then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        shape = RoundedCornerShape(30)
    ) {
        Text(
            text = text,
            style = Typography.labelSmall.copy(MaterialTheme.colorScheme.background)
        )
    }
}



@Composable
fun CardForOneOrderWithoutButton(
    title: String,
    seller: String,
    imageResource: String?,
    navController: NavHostController,
    id: Int,
    total: String,
    price: String,
    onClick: () -> Unit = {}
) {
    val onClick: () -> Unit = remember {
        {
            navController.navigate("${Screen.Product.route}/$id")
        }
    }
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(1f)
            .padding(bottom = 15.dp, end = 16.dp, start = 16.dp)
            .clickable(onClick = onClick)
    ) {
        val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageResource}"

        val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if(imageResource != null)
            {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(13.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            else{
                Image(
                    painter = painterResource(id = R.drawable.imageplaceholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(13.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }


            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    text = seller,
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 25.dp, top = 5.dp),
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = total,
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                    Text(
                        text = price,
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

@Composable
fun SellerCard(
    title: String,
    author: String,
    imageResource: String,
    isLiked: Boolean,
    onClick: () -> Unit,
    navController: NavHostController,
    id: Int
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageResource}"

                val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(13.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Image(
                    painter = painterResource(if (isLiked) R.drawable.srcefull else R.drawable.srce),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable {
                            onClick()
                        }
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(text = author, fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Row(
                    modifier = Modifier.padding(top = 10.dp)
                )
                {
                    CardButton(
                        text = "More info",
                        onClick = {
                            val x = 1
                            navController.navigate("${Screen.Shop.route}/$id/$x")
                        },
                        width = 0.52f,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(32.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    CardButton(
                        text = "Products",
                        onClick = {
                            val x = 2
                            navController.navigate("${Screen.Shop.route}/$id/$x")
                        },
                        width = 1f,
                        modifier = Modifier.height(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun ProductCard(
    title: String,
    price: String,
    imageResource: String,
    navController: NavHostController,
    id: Int,
    onClick: () -> Unit = {}
) {
    val onClick: () -> Unit = remember {
        {
            navController.navigate("${Screen.Product.route}/$id")
        }
    }
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(0.9f)
            .padding(bottom = 15.dp, end = 16.dp)
            .clickable(onClick = onClick)
    ) {
        val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageResource}"

        val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(13.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp,maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 25.dp, top = 5.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                ButtonWithIcon(
                    text = "Show info",
                    onClick = onClick,
                    width = 0.8f,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.secondary,
                    imagePainter = painterResource(id = R.drawable.cart),
                    height = 32
                )
            }
        }
    }
}

@Composable
fun ShopProductCard(imageRes: String?, text: String, price: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .height(200.dp)
            .width(100.dp)
            .padding(8.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageRes != null) {
                val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageRes}"

                val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .clip(RoundedCornerShape(20.dp))
                        .padding(start = 5.dp, end = 5.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.imageplaceholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .clip(RoundedCornerShape(20.dp))
                        .padding(start = 5.dp, end = 5.dp)
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
                modifier = Modifier.padding(top = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = price,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
fun ShopCard(
    title: String,
    price: String,
    imageResource: String,
    navController: NavHostController,
    id: Int,
    workingHours: String,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .clickable {
                val x = 1
                navController.navigate("${Screen.Shop.route}/$id/$x")
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageResource}"

            val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 5.dp),
                    color = MaterialTheme.colorScheme.onTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.padding(top = 25.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = workingHours,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 5.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }
    }
}


@Composable
fun GalleryComponent(
    modifier: Modifier,
    images: List<ImageDataDTO>,
    selectedImage: ImageDataDTO?,
    onImageClick: (ImageDataDTO) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { image ->
            ImageItem(image = image, isSelected = image == selectedImage) {
                onImageClick(image)
            }
        }
    }
}

@Composable
fun ImageItem(image: ImageDataDTO, isSelected: Boolean, onImageClick: () -> Unit) {
    Box(
        modifier = Modifier
            .border(1.dp, if (isSelected) Color.Gray else Color.Transparent)
            .clickable { onImageClick() }
    ) {
        val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${image.image}"

        val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ImageItemForProfilePic(image: String, onEditClick: () -> Unit) {
    val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${image}"

    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)

    Box {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape)
        )


        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.onBackground, shape = CircleShape)
                .padding(8.dp)
                .clickable {
                    onEditClick.invoke()
                }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun ReviewCard(
    username: String,
    imageRes: String?,
    comment: String,
    rating: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))

    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${imageRes}"

                    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }

                if (rating > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            state = rememberLazyListState(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            items(5) { index ->
                                val starIcon = if (index < rating) {
                                    R.drawable.fullstar
                                } else {
                                    R.drawable.emptystar
                                }
                                Image(
                                    painter = painterResource(id = starIcon),
                                    contentDescription = null,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                Text(
                    text = comment,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallElipseAndTitle(title: String, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.elipsemala),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        )
        {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu icon",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp)
                    .align(Alignment.CenterStart)
                    .clickable { scope.launch { drawerState.open() } },
                tint = MaterialTheme.colorScheme.background,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.background),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Tabs(
    onShopsSelected: () -> Unit,
    onFavoritesSelected: () -> Unit,
    selectedColumnIndex: Boolean,
    firstTab: String,
    secondTab: String,
    isFilters: Boolean,
) {
    Row {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onShopsSelected() }
        ) {
            Text(
                text = firstTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (selectedColumnIndex) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 30.dp)
                .clickable { onFavoritesSelected() }
        ) {
            Text(
                text = secondTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (!selectedColumnIndex) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }
    }
}


@Composable
fun FourTabs(
    onFirstTabSelected: () -> Unit,
    onSecondTabSelected: () -> Unit,
    onThirdTabSelected: () -> Unit,
    onFourthTabSelected: () -> Unit,
    selectedColumnIndex: Int,
    firstTab: String,
    secondTab: String,
    thirdTab: String,
    fourthTab: String,
    isFilters: Boolean,
) {
    Row {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onFirstTabSelected() }
        ) {
            Text(
                text = firstTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (selectedColumnIndex == 0) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onSecondTabSelected() }
        ) {
            Text(
                text = secondTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (selectedColumnIndex == 1) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onThirdTabSelected() }
        ) {
            Text(
                text = thirdTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (selectedColumnIndex == 2) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onFourthTabSelected() }
        ) {
            Text(
                text = fourthTab,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = if (!isFilters) 20.sp else 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (selectedColumnIndex == 3) {
                Image(
                    painter = painterResource(id = R.drawable.crtica),
                    contentDescription = "Crtica",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(if (!isFilters) 30.dp else 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

package com.example.front.components

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.front.model.DTO.ImageDataDTO
import com.example.front.navigation.Screen
import com.example.front.ui.theme.DarkBlue
import com.example.front.ui.theme.LightBlue
import com.example.front.ui.theme.MainBlue
import com.example.front.ui.theme.Typography
import java.io.File
import kotlin.io.encoding.Base64
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
    painterResource: Painter,
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
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier.size(25.dp)
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        shape = RoundedCornerShape(20.dp)
    )
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
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth() // Ensure the Text takes up the full width
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
        modifier = Modifier.fillMaxWidth().fillMaxHeight().then(modifier),
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
            .height(32.dp)
            .then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(30)
    ) {
        Text(
            text = text,
            style = Typography.labelSmall.copy(color=MaterialTheme.colorScheme.background)
        )
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
    imageModifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(width)
            .height(32.dp)
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
fun SellerCard(title: String, author: String, imageResource: String,isLiked: Boolean,onClick: () -> Unit ) {
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
                    painter = painterResource(if(isLiked) R.drawable.srcefull else R.drawable.srce),
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
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(text = author, fontSize = 14.sp)
                Row(
                    modifier = Modifier.padding(top = 10.dp)
                )
                {
                    CardButton(
                        text = "More info",
                        onClick = { /*TODO*/ },
                        width = 0.52f,
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    CardButton(
                        text = "Products",
                        onClick = { /*TODO*/ },
                        width = 1f,
                        modifier = Modifier,
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
    id:Int
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(0.9f)
            .padding(bottom = 15.dp, end = 16.dp)
            .clickable { navController.navigate("${Screen.Product.route}/$id") }
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
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 25.dp, top = 5.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                ButtonWithIcon(
                    text = "Add to cart",
                    onClick = { /*TODO*/ },
                    width = 0.8f,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.secondary,
                    imagePainter = painterResource(id = R.drawable.cart)
                )
            }
        }
    }
}

@Composable
fun ShopProductCard(imageRes: Int, text: String, price: String, onClick: () -> Unit = {}) {
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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(start = 5.dp, end = 5.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(color=Color.White),
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                text = price,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light, fontSize = 12.sp,color=Color.White),
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
    id:Int,
    workingHours: String
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .clickable { },
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
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 5.dp),
                    color = MaterialTheme.colorScheme.onTertiary
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
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        }
    }
}


@Composable
fun GalleryComponent(images: List<ImageDataDTO>, modifier: Modifier) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { image ->
            ImageItem(image = image)
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ImageItem(image: ImageDataDTO) {
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
fun SmallElipseAndTitle(title:String) {
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
                    .align(Alignment.CenterStart),
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
                    fontSize = if(!isFilters) 20.sp else 16.sp
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
                    if(!isFilters) 20.sp else 16.sp
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
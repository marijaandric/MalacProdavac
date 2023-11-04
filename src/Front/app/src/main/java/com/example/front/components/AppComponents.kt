package com.example.front.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.ui.theme.LightBlue
import com.example.front.ui.theme.MainBlue
import com.example.front.ui.theme.Typography

@Composable
fun TitleTextComponent(value:String){
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
    isPassword: Boolean = false
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
        keyboardOptions = if (isPassword) {
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        } else {
            KeyboardOptions.Default
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(valuee: String,placeh:String, onValueChangee: (String) -> Unit) {
    var value = valuee

    OutlinedTextField(value = value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.onSurface,
            containerColor = Color.White),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        onValueChange = onValueChangee,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier.height(50.dp),
        placeholder  = { Text(text = placeh) }
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
@Composable
fun LogoImage(painterResource: Painter, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource,
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .then(modifier) // Combine the existing modifier with the one passed as an argument
    )
}


// -- BUTTONS --
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
fun MediumBlueButton(text:String,onClick: () -> Unit,width:Float,modifier: Modifier) {
    val primaryColor = MainBlue
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(8.dp)
            .height(48.dp).
            then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        shape = RoundedCornerShape(20)
    ) {
        Text(text = text,
            style = Typography.bodyLarge)
    }
}

@Composable
fun CardButton(text:String,onClick: () -> Unit,width:Float,modifier: Modifier,color : Color) {
    val primaryColor = MainBlue
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .height(38.dp).
            then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(20)
    ) {
        Text(text = text,
            style = Typography.bodyLarge)
    }
}


@Composable
fun BigBlueButton(text:String,onClick: () -> Unit,width:Float,modifier: Modifier) {
    val primaryColor = MainBlue
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(8.dp)
            .height(58.dp).
            then(modifier),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        shape = RoundedCornerShape(30)
    ) {
        Text(text = text,
            style = Typography.labelSmall)
    }
}



@C
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)omposable
                        fun SellerCard(title: String, author: String, imageResource: Int) {
                            Card(
                                modifier = Modifier
                                    .width(320.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(id = imageResource),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                    .padding(13.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = author, fontSize = 10.sp)
                Row(
                    modifier = Modifier.padding(top = 10.dp)
                )
                {
                    CardButton(text = "More info", onClick = { /*TODO*/ }, width = 0.52f, modifier = Modifier.padding(end=8.dp), color = MaterialTheme.colorScheme.secondary)
                    CardButton(text = "Products", onClick = { /*TODO*/ }, width = 1f, modifier = Modifier, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}


@Composable
fun ProductCard(title: String, price: String, imageResource: Int) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(bottom = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = price, fontSize = 10.sp, modifier = Modifier.padding(bottom=25.dp))
                CardButton(text = "Add to cart", onClick = { /*TODO*/ }, width = 0.9f, modifier = Modifier, color = MaterialTheme.colorScheme.secondary)
            }

        }


    }
}

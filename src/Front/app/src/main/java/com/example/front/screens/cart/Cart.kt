package com.example.front.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.ProductCard
import com.example.front.components.TitleTextComponent
import com.example.front.model.product.ProductInCart
import com.example.front.screens.home.CardData
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.cart.CartViewModel
import java.text.DecimalFormat

@Composable
fun Cart(
    viewModel: CartViewModel,
    navController: NavHostController
) {
    LaunchedEffect(key1 = true) {
        viewModel.getCartProducts()
    }
    val cartState = viewModel.state.value
    val isButtonVisible by remember { mutableStateOf(false) }
    var sum by remember { mutableDoubleStateOf(0.0) }


    LaunchedEffect(cartState.isLoading) {
        if (!cartState.isLoading) {
            sum = calculateTotal(cartState.products)
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                ,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.elipsemala),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    contentScale = ContentScale.FillWidth,


                )
                Text(
                    text = "Cart",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .offset(y = 40.dp),
                    style = Typography.titleLarge,
                    color = Color.White
                )
            }
//            Spacer(Modifier.height(200.dp))

//            if (cartState.isLoading) {
//                // Prikazati indikator ucitavanja ako podaci jos nisu usitani
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//            }

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .weight(1f)
            ) {
                // Grupisanje proizvoda po prodavnicama
                val groupedProducts = cartState.products.groupBy { it.shopName }

                groupedProducts.forEach { (shopName, products) ->
                    // Header red za prikaz imena prodavnice
                    item {
                        Text(
                            text = shopName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    // Prikaz svakog proizvoda iz trenutne prodavnice
                    items(products) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Image(painter = rememberAsyncImagePainter(model = "http://softeng.pmf.kg.ac.rs:10015/images/${product.image}"), contentDescription = "",
                                modifier = Modifier
                                    .width(96.dp)
                                    .height(96.dp)
                                    .border(1.dp, Color.Red, RectangleShape)
                            )
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 2.dp)
                                ,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    product.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
//                                Text(product.shopName)
//                                var txt = "%2.f".format(product.quantity)
//                                txt += " " + product.metric
//                                Text(text = "%2.f %s".format(product.quantity ?: 0.0, product.metric), Modifier, Color(0xFFE15F26))
//                                Text(text = txt, Modifier, Color(0xFFE15F26))
//                                Text(text = product.quantity.toString())
                                Text(DecimalFormat("#.00").format(product.quantity) + " " + product.metric, Modifier, Color(0xFFE15F26))
                                Text(DecimalFormat("#.00").format(product.price * product.quantity).toString() + " rsd")
//                                Text("Slika: " + product.image)
                            }
                        }
//                        sum += product.price * product.quantity
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Surface(
                color= Color.White,
                modifier = Modifier
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total: ${DecimalFormat("#.00").format(sum)} rsd",
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                        ,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF457FA8)),
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                    ) {
                        Text(text = "Checkout", fontSize = 20.sp, modifier = Modifier,color = Color.White,

                            fontFamily = FontFamily(Font(R.font.lexend)),
                            fontWeight = FontWeight(300))
                    }
                }
            }

        }

//        Box(
//                modifier = Modifier
////            .fillMaxSize()
//                    .fillMaxWidth()
////                    .align(Alignment.BottomCenter)
////            .offset(y = if (isButtonVisible) (-20).dp else 0.dp)
//        ,
//
////        color = Color.White
//        ) {
//            //za donji deo sa CHECKOUT DUGMETOM
//            Button(onClick = { /*TOD    O*/ }) {
//                Text(text = "DUGME")
//            }
//        }
    }

//    Surface(
//        modifier = Modifier
////            .fillMaxSize()
//            .fillMaxWidth()
////            .offset(y = if (isButtonVisible) (-20).dp else 0.dp)
//        ,
//
////        color = Color.White
//    ) {
//        //za donji deo sa CHECKOUT DUGMETOM
//        Button(onClick = { /*TOD    O*/ }) {
//            Text(text = "DUGME")
//        }
//    }
}

private fun calculateTotal(products: List<ProductInCart>): Double {
    return products.sumOf { it.price * it.quantity }
}
package com.example.front.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.product.ProductInCart
import com.example.front.navigation.Screen
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.cart.CartViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
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
    val coroutineScope = rememberCoroutineScope()
    val cartProducts = cartState.products
    sum = cartProducts.sumOf { it.price * it.quantity }
    val groupedProducts = cartState.products.groupBy { it.shopName }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        viewModel.dataStoreManager
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SmallElipseAndTitle(title = "Cart", drawerState)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                        .weight(1f)
                ) {

                    if (groupedProducts.isEmpty()) {
                        item {
                            Text(
                                text = "Your cart is empty",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 8.dp)

                            )
                        }

                    }
                    groupedProducts.forEach { (shopName, products) ->

                        item {
                            Text(
                                text = shopName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 8.dp)

                            )
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFF3A5163))
                                    .padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Prikaz svakog proizvoda iz trenutne prodavnice
                        items(products) { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .height(96.dp),
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = "http://softeng.pmf.kg.ac.rs:10015/images/${product.image}"),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(96.dp)
                                        .height(96.dp)
//                                    .border(1.dp, Color.Red, RectangleShape)
                                        .graphicsLayer(
                                            clip = true,
                                            shape = RoundedCornerShape(16.dp)
                                        )

                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        product.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        DecimalFormat("#.00").format(product.quantity) + " " + product.metric,
                                        Modifier,
                                        Color(0xFFE15F26)
                                    )
                                    Text(
                                        DecimalFormat("#.00").format(product.price * product.quantity)
                                            .toString() + " rsd"
                                    )
//                                Text("Slika: " + product.image)
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(24.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.bin),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(Color(0xFFAC1D1D)),
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .height(16.dp)
                                            .clickable {

                                                coroutineScope.launch {
                                                    sum -= product.price * product.quantity
                                                    viewModel.removeProductFromCart(product.id)
                                                }

                                            }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }
                }

                Surface(
                    color = Color.White,
                    modifier = Modifier
                ) {

                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total: ${DecimalFormat("0.00").format(sum)} rsd",
                            modifier = Modifier
                                .padding(vertical = 14.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = {
                                navController.navigate(route = Screen.Checkout.route)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF457FA8)),
                            modifier = Modifier
                                .padding(vertical = 14.dp),
                            enabled = cartProducts.isNotEmpty()
                        ) {
                            Text(
                                text = "Checkout",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.lexend)),
                                fontWeight = FontWeight(300),
                                modifier = Modifier
                            )
                        }
                    }
                }

            }
        }
    }
}
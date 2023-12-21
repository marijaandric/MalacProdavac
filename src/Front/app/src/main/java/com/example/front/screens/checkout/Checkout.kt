package com.example.front.screens.checkout

import ToastHost
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.DTO.NewOrder
import com.example.front.model.product.ProductInOrder
import com.example.front.model.user.CreditCardModel
import com.example.front.navigation.Screen
import com.example.front.screens.cart.CreditCard
import com.example.front.viewmodels.checkout.CheckoutViewModel
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import rememberToastHostState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDatePicker(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    navController: NavHostController,
    totalsByShop: String?
) {

    LaunchedEffect(key1 = true) {
        val shopsTotals = parseTotalsByShop(totalsByShop ?: "")
        viewModel.getCheckoutData(shopsTotals)
//        viewModel.getAllCreditCards()
    }

    val toastHostState = rememberToastHostState()

    val cartProducts = viewModel.stateProducts.value.products.groupBy { it.shopId }
    val date = remember { mutableStateOf(LocalDate.now()) }
    val isOpen = remember { mutableStateOf(false) }
//    val isOpenMap = remember { mutableStateMapOf<Int, Boolean>() }
//    val isOpenMap = remember { mutableStateMapOf<Int, MutableState<Boolean>>() }
//    val dateMap = remember { mutableStateMapOf<Int, MutableState<LocalDate>>() }
    val isOpenMap = remember { mutableStateMapOf<Int, Boolean>() }
    val dateMap = remember { mutableStateMapOf<Int, LocalDate>() }
    var payUsingCard by remember { mutableStateOf(false) }

    val checkoutState = viewModel.state.value
    val shops = viewModel.shopsForCheckout.value
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val creditCards: List<CreditCardModel> = viewModel.creditCards.value

    //sa listof unit pokrece samo jednom (kad se pokrene stranica prvi put)
    LaunchedEffect(key1 = listOf<Unit>()) {
        println("USAO")
        shops.forEach { shop ->
            isOpenMap[shop.id] = false
            dateMap[shop.id] = LocalDate.now()
        }
    }

    Sidebar(
        drawerState,
        navController,
        viewModel.dataStoreManager
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SmallElipseAndTitle(title = "Checkout", drawerState)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(top=100.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Text(text = "On pickup")
                    Switch(
                        checked = payUsingCard,
                        onCheckedChange = {
                            payUsingCard = it
                        }
                    )
                    Text(text = "Credit/Debit Card")
                }

                if (payUsingCard) {
                    //kartice

                    coroutineScope.launch {
                        viewModel.getAllCreditCards()
                    }
                    var selectedCardIndex by remember { mutableStateOf(0) }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        creditCards.forEachIndexed { index, creditCard ->
                            println("Credit Card: $creditCard")
                            item {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedCardIndex = index
                                        }
                                ) {
                                    CreditCard(creditCard = creditCard)

                                    if (index == selectedCardIndex) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF043A64), // Boja ikone
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .padding(top = 12.dp, end = 12.dp)
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    //dodavanje novih kartica
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
//                            .weight(1f)
                        ,
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(15.dp)
                        ) {
                            ButtonWithIcon(
                                text = "Add Credit/Debit Card",
                                onClick = { navController.navigate(route = Screen.NewCreditCard.route) },
                                width = 0.8f,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.primary,
                                imagePainter = painterResource(id = R.drawable.ion_card_outline),
                                height = 50
                            )
                        }
//                        Row(
//                            modifier = Modifier
//                                .padding(15.dp)
//                        ) {
//                            ButtonWithIcon(
//                                text = "Paypal",
//                                onClick = { /*navController.navigate()*/ },
//                                width = 0.8f,
//                                modifier = Modifier.padding(10.dp),
//                                color = MaterialTheme.colorScheme.primary,
//                                imagePainter = painterResource(id = R.drawable.logos_paypal),
//                                height = 50
//                            )
//                        }
                    }
                }

                //Spisak prodavnica
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                        .padding(all = 16.dp)
//                                .weight(1f)
                ) {
                    shops.forEach { shop ->
//                                item {

                        //slika, mapa, adresa, ukupno?, deliveri-pickup,
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .shadow(4.dp)
                                .clip(RoundedCornerShape(5.dp)),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
//                                        .border(1.dp, Color.Red, RectangleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ellipsebrojdva),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                                Column {
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        text = shop.name,
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.lexend)),
                                        fontWeight = FontWeight(500),
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
//                                        .border(1.dp, Color.Blue, RectangleShape)
                            ) {
                                // mapa
                                Box(
                                    modifier = Modifier
                                        .height(130.dp)
                                        .width(130.dp)
                                        .padding(8.dp)
                                        .border(1.dp, Color.Red, RectangleShape)
                                ) {}
                                // adresa
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(146.dp)
//                                            .border(1.dp, Color.Blue, RectangleShape)
                                    ,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = shop.address.substring(
                                            0,
                                            shop.address.length - 8
                                        ),
                                        modifier = Modifier.padding(top = 20.dp)

                                    )
                                    Text(
                                        text = "Total: ${shop.total} rsd",
                                        modifier = Modifier.padding(bottom = 20.dp)
                                    )
                                }
                            }


                            // slider delivery-pickup
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
//                                    .border(1.dp, Color.Yellow, RectangleShape)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Text(text = "Delivery")
                                Switch(
                                    checked = shop.selfpickup,
                                    onCheckedChange = {
                                        viewModel.updateSelfPickup(shop.id, it)
                                        println(shop.selfpickup)
                                    }
                                )
                                Text(text = "Self pickup")
                            }
                            // opciono datepicker
                            if (shop.selfpickup) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    OutlinedTextField(
                                        readOnly = true,
                                        value = dateMap[shop.id]?.format(DateTimeFormatter.ISO_DATE)
                                            ?: "",
                                        label = { Text("Date") },
                                        onValueChange = {},
                                        shape = RoundedCornerShape(20.dp))

                                    IconButton(
                                        onClick = { isOpenMap[shop.id] = true }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Calendar"
                                        )
                                    }
                                }

                                if (isOpenMap[shop.id] == true) {
                                    ModalDatePicker(
                                        onAccept = {
                                            isOpenMap[shop.id] = false

                                            if (it != null) { // Set the date
                                                dateMap[shop.id] = Instant
                                                    .ofEpochMilli(it)
                                                    .atZone(ZoneId.of("UTC"))
                                                    .toLocalDate()
                                            }
                                        },
                                        onCancel = {
                                            isOpenMap[shop.id] = false //close dialog
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(22.dp))

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
                        var address by remember { mutableStateOf("") }
                        var city by remember { mutableStateOf("") }

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                            ,
                            value = address,
                            label = { Text("Shipping Address") },
                            onValueChange = {address = it},
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                            ,
                            value = city,
                            label = { Text("City") },
                            onValueChange = {city = it},
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )

                        Button(
                            enabled = address.isNotEmpty() && city.isNotEmpty(),
                            onClick = {
                                var userId: Int?
                                coroutineScope.launch {
                                    userId = viewModel.dataStoreManager.getUserIdFromToken()
                                    if (userId != null) {
                                        //// izmeniti polja za newOrder
                                        val orders: List<NewOrder> = shops.map { shop ->
                                            NewOrder(
                                                userId = userId!!,
                                                shopId = shop.id,
                                                paymentMethod = if (payUsingCard) 3 else 1, // 3-kartica, 1-pouzecem
                                                deliveryMethod = if (shop.selfpickup) 1 else 2, // 1-self, 2-delivery
                                                shippingAddress = "$address, $city, Srbija",
                                                pickupTime = if (shop.selfpickup) dateMap[shop.id].toString()+"T10:00:00" else null,
                                                products = cartProducts[shop.id]!!.map {
                                                    ProductInOrder(it.id, it.sizeId, it.quantity)
                                                }
                                            )
                                        }
                                        println("ORDERS: ${orders[0]}")
                                        println("ORDER: ${orders[0].products[0].id}")

                                        val successful = viewModel.insertOrders(orders)
                                        if (successful) {
                                            //// brise korpu vraca na pocetnu ili na orders
                                            println("USPESNO SLANJE")
                                            coroutineScope.launch {
                                                try {
                                                    toastHostState.showToast("Orders placed successfully", Icons.Default.Check)
                                                } catch (e: Exception) {
                                                    Log.e("ToastError", "Error showing toast", e)
                                                }
                                            }
                                            ////navController na orders ili home
                                        } else {
                                            println("NEUSPESNO SLANJE")
                                            coroutineScope.launch {
                                                try {
                                                    toastHostState.showToast("Please check all fields", Icons.Default.Clear)
                                                } catch (e: Exception) {
                                                    Log.e("ToastError", "Error showing toast", e)
                                                }
                                            }

                                        }

                                    }
                                }

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF457FA8)),
                            modifier = Modifier
                                .padding(vertical = 14.dp),
//                            enabled = cartProducts.isNotEmpty()
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
//            }
        }
    }
    ToastHost(hostState = toastHostState)
}

private fun parseTotalsByShop(totalsByShop: String): Map<Int, Double> {
    val resultMap = mutableMapOf<Int, Double>()

    try {
        val jsonObject = JsonParser.parseString(totalsByShop).asJsonObject
        for ((key, value) in jsonObject.entrySet()) {
            resultMap[key.toInt()] = value.asDouble
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return resultMap
}
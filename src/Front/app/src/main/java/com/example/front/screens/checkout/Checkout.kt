package com.example.front.screens.checkout

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.navigation.Screen
import com.example.front.screens.cart.CreditCard
import com.example.front.viewmodels.checkout.CheckoutViewModel
import com.google.gson.JsonParser
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
    }

    val date = remember { mutableStateOf(LocalDate.now())}
    val isOpen = remember { mutableStateOf(false)}


    val checkoutState = viewModel.state.value
    val shops = viewModel.shopsForCheckout.value
    val scrollState = rememberScrollState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        OutlinedTextField(
                            readOnly = true,
                            value = date.value.format(DateTimeFormatter.ISO_DATE),
                            label = { Text("Date") },
                            onValueChange = {})

                        IconButton(
                            onClick = { isOpen.value = true } // show de dialog
                        ) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                        }
                    }
                    if (isOpen.value) {
                        ModalDatePicker(
                            onAccept = {
                                isOpen.value = false // close dialog

                                if (it != null) { // Set the date
                                    date.value = Instant
                                        .ofEpochMilli(it)
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDate()
                                }
                            },
                            onCancel = {
                                isOpen.value = false //close dialog
                            }
                        )
                    }
//                    DatePicker(state = datePickerState, )

                        //kartice
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            item {
                                CreditCard()
                            }
                            item {
                                CreditCard()
                            }
                            item {
                                CreditCard()
                            }
                            item {
                                CreditCard()
                            }
                        }

                        //dodavanje novih kartica
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
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
                            Row(
                                modifier = Modifier
                                    .padding(15.dp)
                            ) {
                                ButtonWithIcon(
                                    text = "Paypal",
                                    onClick = { /*navController.navigate()*/ },
                                    width = 0.8f,
                                    modifier = Modifier.padding(10.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    imagePainter = painterResource(id = R.drawable.logos_paypal),
                                    height = 50
                                )
                            }
                        }


                        //Spisak prodavnica
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 0.dp)
//                                .weight(1f)
                        ) {
                            shops.forEach { shop ->
//                                item {

                                //slika, mapa, adresa, ukupno?, deliveri-pickup,
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
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
                                                .fillMaxWidth(),
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
                                                text = shop.address.substring(0,shop.address.length-8),
                                                modifier = Modifier.padding(top = 20.dp)

                                            )
                                            Text(
                                                text = "Ukupno: ${shop.total} rsd",
                                                modifier = Modifier.padding(bottom = 20.dp)
                                            )
                                        }
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
                                    Text(text = "Biranje datuma")
                                }
                            }
                        }


                }
//            }
        }
    }
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
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.front.navigation.Screen
import com.example.front.screens.cart.CreditCard
import com.example.front.ui.theme.Typography
import com.example.front.viewmodels.checkout.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    navController: NavHostController
) {


    LaunchedEffect(key1 = true) {
        viewModel.getCheckoutData()
    }

    val checkoutState = viewModel.state.value

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.elipsemala),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,


                    )
                Text(
                    text = "Checkout",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .offset(y = 40.dp),
                    style = Typography.titleLarge,
                    color = Color.White
                )
            }

            //Spisak prodavnica
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .weight(1f)
            ) {
                checkoutState.forEach { shop ->
                    item {
                        Text(
                            text = shop.toString()
                        )
                    }
                }
            }

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
//                        .border(1.dp, Color.Red, RectangleShape)
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
                            text = "Naziv Prodavnice",
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
//                        .border(1.dp, Color.Blue, RectangleShape)
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
//                            .border(1.dp, Color.Blue, RectangleShape)
                        ,
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Adresa Prodavnice", modifier = Modifier.padding(top = 20.dp))
                        // cena?
                        Text(text = "Ukupno: 400 rsd", modifier = Modifier.padding(bottom = 20.dp))
                    }
                }

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
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Row(modifier = Modifier
                        .padding(15.dp)) {
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
                    Row(modifier = Modifier
                        .padding(15.dp)) {
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
            }
            // slider delivery-pickup
            var checked by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                        .border(1.dp, Color.Yellow, RectangleShape)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(text = "Delivery")
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    }
                )
                Text(text = "Self pickup")
            }
            // opciono datepicker
            if (checked) {
                Text(text = "Biranje datuma")
//                    TextField(value = "", onValueChange = {})
            }
        }

    }
}


package com.example.front.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.user.CreditCardModel
import com.example.front.viewmodels.checkout.CheckoutViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCreditCartScreen(navController: NavHostController, viewModel: CheckoutViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navController,
        viewModel.dataStoreManager
    ) {
        CardList(drawerState, viewModel, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardList(drawerState: DrawerState, viewModel: CheckoutViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallElipseAndTitle("Add New Card", drawerState)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    CreditCard()
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    CreditCardInput(viewModel,navController)
                }
            }
        }
    }
}

val gradientColors = listOf(
    Color(0xEA97F8), // Replace with your start color
    Color(0xA0A7E3)  // Replace with your end color
)

@Composable
fun CreditCard(creditCard: CreditCardModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(10.dp)
            .size(300.dp, 150.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize() // Fill the entire space of the Card
            .background(Brush.linearGradient(gradientColors))
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ){ // Add padding inside the Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), // Add space below the row
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val cardType = determineCardType("5111")
                    when (cardType) {
                        "Visa" -> {
                            Image(
                                painter = painterResource(id = R.drawable.visa),
                                contentDescription = "Visa",
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(30.dp)
                            )
                        }

                        "Mastercard" -> {
                            Image(
                                painter = painterResource(id = R.drawable.mastercard),
                                contentDescription = "MasterCard",
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(30.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Text("**** **** **** ${creditCard.cardNumber.takeLast(4)}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), // Add space above the row
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(creditCard.nameOnCard)
                    Text(creditCard.expDate)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardInput(viewModel: CheckoutViewModel, navController: NavHostController) {
    var cardNumber by remember { mutableStateOf("") }
    var nameOnCard by remember { mutableStateOf("") }
    var expDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(10.dp)
            .size(350.dp, 300.dp)
    ) {
        TextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Credit Card Number") },
            modifier = Modifier
                .padding(5.dp)
                .background(Color.White)
                .fillMaxWidth()
        )
        TextField(
            value = nameOnCard,
            onValueChange = { nameOnCard = it },
            label = { Text("Name on Card") },
            modifier = Modifier
                .padding(5.dp)
                .background(Color.White)
                .fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = expDate,
                onValueChange = { expDate = it },
                label = { Text("Exp. Date (MM/YY)") },
                modifier = Modifier
                    .padding(5.dp)
                    .background(Color.White)
                    .weight(1f)
            )
            TextField(
                value = cvv,
                onValueChange = { cvv = it },
                label = { Text("CVV") },
                modifier = Modifier
                    .padding(5.dp)
                    .background(Color.White)
                    .weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                // Validacija i ubacivanje kartice u bazu
                if (cardNumber.isNotBlank() && nameOnCard.isNotBlank() && expDate.isNotBlank() && cvv.isNotBlank()) {
                    val creditCard = CreditCardModel()
                    creditCard.cardNumber = cardNumber
                    creditCard.nameOnCard = nameOnCard
                    creditCard.expDate = expDate
                    creditCard.cvv = cvv

                    // Ubacivanje kartice u Realm bazu
                    coroutineScope.launch {
                        viewModel.insertCreditCard(creditCard)
                        // navigira na checkout
                        navController.popBackStack()
                    }
                } else {
                }
            }, modifier = Modifier.weight(0.5f)) {
                Text("Add Card")
            }
        }
    }
}

fun determineCardType(cardNumber: String): String {
    return when {
        cardNumber.startsWith("4") -> "Visa"
        cardNumber.startsWith("5") && cardNumber.length >= 2 -> {
            val secondDigit = cardNumber[1].digitToInt()
            if (secondDigit in 1..5) "Mastercard" else "Unknown"
        }

        else -> "Unknown"
    }
}
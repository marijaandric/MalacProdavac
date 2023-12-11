package com.example.front.screens.delivery

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.viewmodels.delivery.RouteDetailsViewModel

@Composable
fun RouteDetailsScreen(
    navHostController: NavHostController,
    routedetailsViewModel: RouteDetailsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navHostController,
        routedetailsViewModel.dataStoreManager
    ) {
        val scrollState = rememberScrollState()
        val stops = listOf(
            "Raška 6, Kragujevac" to "Domaćinstvo Jovanović",
            "Vlastimira Petrovića 12, Miločaj" to "Radionica Andrić"
        )
        val moreStops = "2 more stops - click to expand"
        val deliveryDetails = "Delivery details >"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SmallElipseAndTitle("Route details", drawerState)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "KRAGUJEVAC - KRALJEVO",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "26/11/2023 11:00",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Map Placeholder")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Route stops",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                RouteStopsSection(
                    stops = listOf(
                        "Raška 6, Kragujevac" to "Domaćinstvo Jovanović",
                        "Vlastimira Petrovića 12, Miločaj" to "Radionica Andrić"
                    ),
                    additionalStops = listOf(
                        "Stop 3 Address" to "Stop 3 Description",
                        "Stop 4 Address" to "Stop 4 Description"
                    )
                )
            }
        }
    }
}

@Composable
fun RouteStopsSection(stops: List<Pair<String, String>>, additionalStops: List<Pair<String, String>>) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Route stops",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        stops.forEach { (address, description) ->
            RouteStopItem(address, description)
        }

        if (expanded) {
            additionalStops.forEach { (address, description) ->
                RouteStopItem(address, description)
            }
        }

        Text(
            text = if (expanded) "Less stops" else "2 more stops - click to expand",
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp)
        )

        Divider(color = Color(0xFFDADADA), thickness = 1.dp)
        Text(
            text = "Delivery details >",
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier
                .clickable { /* Handle click here to navigate to delivery details */ }
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun RouteStopItem(address: String, description: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Canvas(modifier = Modifier
            .size(12.dp)
            .align(Alignment.Top), onDraw = {
            drawCircle(color = Color.Gray)
        })
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = address,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun MoreStopsText(moreStops: String) {
    Text(
        text = moreStops,
        fontSize = 14.sp,
        color = Color.Blue,
        modifier = Modifier
            .clickable { /* Handle click here to expand the list */ }
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ExpandableText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Blue,
        modifier = Modifier
            .clickable { /* Handle click here to navigate to delivery details */ }
            .padding(vertical = 8.dp)
    )
}
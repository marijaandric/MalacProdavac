package com.example.front.screens.delivery

import android.content.Context
import android.util.Log
import android.view.ViewGroup
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.front.components.Sidebar
import com.example.front.components.SmallElipseAndTitle
import com.example.front.model.DTO.Stop
import com.example.front.viewmodels.delivery.RouteDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.Color as AndroidColor

@Composable
fun RouteDetailsScreen(
    navHostController: NavHostController,
    routedetailsViewModel: RouteDetailsViewModel
) {

    LaunchedEffect(Unit) {
        routedetailsViewModel.getRouteDetails(1)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Sidebar(
        drawerState,
        navHostController,
        routedetailsViewModel.dataStoreManager
    ) {
        if (routedetailsViewModel.state.value.isLoading) {
            CircularProgressIndicator()
        } else if (routedetailsViewModel.state.value.error.isNotEmpty()) {
            Log.d("ERROR", routedetailsViewModel.state.value.error)
        } else {
            val scrollState = rememberScrollState()
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        routedetailsViewModel.state.value.details?.locations?.let {
                            Text(
                                text = it,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = routedetailsViewModel.state.value.details?.startDate + " " + routedetailsViewModel.state.value.details?.startTime,
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
                            routedetailsViewModel.state.value.details?.stops?.let { Osm(it) }
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
}

@Composable
fun Osm(stops: List<Stop>) {
    val context = LocalContext.current
    val yourUserAgent = "YourUserAgentName"
    org.osmdroid.config.Configuration.getInstance().userAgentValue = yourUserAgent
    org.osmdroid.config.Configuration.getInstance()
        .load(context, context.getSharedPreferences("osmdroid", 0))

    // State to hold route points
    var routePoints by remember { mutableStateOf(listOf<GeoPoint>()) }

    LaunchedEffect(stops) {
        routePoints = withContext(Dispatchers.IO) {
            fetchRoute(stops)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView(context) },
        update = { mapView ->
            // Create and update the polyline for the route
            val route = Polyline().apply {
                setPoints(routePoints)
                color = android.graphics.Color.BLUE
            }

            mapView.overlays.clear()
            mapView.overlays.add(route)

            routePoints.forEach { point ->
                val marker = Marker(mapView).apply {
                    position = point
                }
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
            if (routePoints.isNotEmpty()) {
                mapView.controller.setCenter(routePoints.first())
            }
        }
    )
}

fun fetchRoute(stops: List<Stop>): List<GeoPoint> {
    val apiKey = "5b3ce3597851110001cf6248fc9e15141a974854817f85dee9e19f1f"
    val urlString = "https://api.openrouteservice.org/v2/directions/driving-car"
    val geoPoints = mutableListOf<GeoPoint>()

    try {
        // Prepare the request body with coordinates
        val requestBody = JSONObject().apply {
            put("coordinates", JSONArray(stops.map { listOf(it.longitude, it.latitude) }))
        }

        // Establish connection
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", apiKey)
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        // Send request
        connection.outputStream.use { os ->
            val input = requestBody.toString().toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }

        // Read response
        connection.inputStream.use { stream ->
            val response = stream.bufferedReader().use(BufferedReader::readText)
            val jsonResponse = JSONObject(response)
            val coordinates = jsonResponse.getJSONObject("routes")
                .getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates")

            for (i in 0 until coordinates.length()) {
                val coordinate = coordinates.getJSONArray(i)
                val latitude = coordinate.getDouble(1)
                val longitude = coordinate.getDouble(0)
                geoPoints.add(GeoPoint(latitude, longitude))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle errors appropriately
    }

    return geoPoints
}

private fun mapView(context: Context): org.osmdroid.views.MapView {
    val mapView = org.osmdroid.views.MapView(context)
    mapView.controller.setZoom(18.0)
    mapView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    return mapView
}


@Composable
fun RouteStopsSection(
    stops: List<Pair<String, String>>,
    additionalStops: List<Pair<String, String>>
) {
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
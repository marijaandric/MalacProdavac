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
import androidx.compose.material3.MaterialTheme
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
import com.example.front.model.DTO.Item
import com.example.front.model.DTO.RouteDetails
import com.example.front.model.DTO.RouteStopsSection
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

@Composable
fun RouteDetailsScreen(
    navHostController: NavHostController,
    routedetailsViewModel: RouteDetailsViewModel,
    routeID: Int
) {

    LaunchedEffect(Unit) {
        routedetailsViewModel.getRouteDetails(routeID)
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

                    val routeStops =
                        createRouteStopsSection(routedetailsViewModel.state.value.details?.stops!!)

                    val mainStops = routeStops.stops.take(2)

                    val additionalStops = routeStops.stops.drop(2)

                    RouteStopsSection(
                        stops = mainStops,
                        additionalStops = additionalStops,
                        routedetailsViewModel
                    )

                }
            }
        }
    }
}

fun createRouteStopsSection(allStops: List<Stop>): RouteStopsSection {
    // Transform Stop objects into pairs of address and shopName
    val transformedStops = allStops.map { stop ->
        stop.address to (stop.shopName ?: "Unknown Shop")
    }

    // Split the list into main stops and additional stops
    val (mainStops, additionalStops) = transformedStops.partition {
        it == transformedStops.first() || it == transformedStops.getOrNull(
            1
        )
    }

    return RouteStopsSection(
        stops = mainStops,
        additionalStops = additionalStops
    )
}

@Composable
fun Osm(stops: List<Stop>) {
    val context = LocalContext.current
    val yourUserAgent = "YourUserAgentName"
    org.osmdroid.config.Configuration.getInstance().userAgentValue = yourUserAgent
    org.osmdroid.config.Configuration.getInstance()
        .load(context, context.getSharedPreferences("osmdroid", 0))

    var routePoints by remember { mutableStateOf(listOf<GeoPoint>()) }
    var isLoading by remember { mutableStateOf(true) } // Loading state

    LaunchedEffect(stops) {
        isLoading = true
        routePoints = withContext(Dispatchers.IO) {
            fetchRoute(stops)
        }
        isLoading = false
    }

    if (isLoading) {
        // Display a loading indicator
        CircularProgressIndicator()
    } else {
        // Display the map view
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView(context) },
            update = { mapView ->
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

//                    mapView.overlays.add(marker)
                }

                mapView.invalidate()
                if (routePoints.isNotEmpty()) {
                    mapView.controller.setCenter(routePoints.first())
                }
            }
        )
    }
}


fun fetchRoute(stops: List<Stop>): List<GeoPoint> {
    val apiKey =
        "5b3ce3597851110001cf6248fc9e15141a974854817f85dee9e19f1f" // Replace with your actual API key
    val urlString = "https://api.openrouteservice.org/v2/directions/driving-car/geojson"
    val geoPoints = mutableListOf<GeoPoint>()

    try {
        // Prepare the request body with coordinates
        val requestBody = JSONObject().apply {
            put("coordinates", JSONArray(stops.map { listOf(it.longitude, it.latitude) }))
        }

        Log.d("fetchRoute", "Request Body: ${requestBody.toString()}")

        // Establish connection
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", apiKey)
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/geo+json")
        connection.doOutput = true

        Log.d("fetchRoute", "Sending request to $urlString")

        // Send request
        connection.outputStream.use { os ->
            val input = requestBody.toString().toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }

        Log.d("fetchRoute", "Request sent")

        // Check response code
        val responseCode = connection.responseCode
        Log.d("fetchRoute", "Response Code: $responseCode")

        // Read response based on the response code
        val response = if (responseCode in 200..299) {
            // Success response
            connection.inputStream.bufferedReader().use(BufferedReader::readText)
        } else {
            // Error response
            connection.errorStream.bufferedReader().use(BufferedReader::readText)
        }

        Log.d("fetchRoute", "Response: $response")

        if (responseCode in 200..299) {
            val jsonResponse = JSONObject(response)
            val features = jsonResponse.getJSONArray("features")

            // Assuming the first feature is the one we are interested in
            if (features.length() > 0) {
                val feature = features.getJSONObject(0)
                val geometry = feature.getJSONObject("geometry")
                val coordinatesArray = geometry.getJSONArray("coordinates")

                for (i in 0 until coordinatesArray.length()) {
                    val coordinatePair = coordinatesArray.getJSONArray(i)
                    val longitude = coordinatePair.getDouble(0)
                    val latitude = coordinatePair.getDouble(1)
                    geoPoints.add(GeoPoint(latitude, longitude))
                }
            }
        } else {
            Log.e("fetchRoute", "Error in fetching route: $response")
        }
    } catch (e: Exception) {
        Log.e("fetchRoute", "Exception: ${e.message}")
        e.printStackTrace()
    }

    Log.d("fetchRoute", "GeoPoints retrieved: ${geoPoints.size}")
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
    additionalStops: List<Pair<String, String>>,
    viewModel: RouteDetailsViewModel
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

        ExpandableText(
            additionalStops = additionalStops,
            expanded = expanded,
            onToggleExpand = { expanded = !expanded }
        )

        Divider(color = Color(0xFFDADADA), thickness = 1.dp)
        var showRouteDetailCard by remember { mutableStateOf(false) }

        Text(
            text = "Delivery details >",
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier
                .clickable { showRouteDetailCard = !showRouteDetailCard }
                .padding(vertical = 8.dp)
        )

        // Show or hide the RouteDetailCard based on the state
        if (showRouteDetailCard) {
            RouteDetailCard(viewModel.state.value.details!!)
        }
    }
}

@Composable
fun ExpandableText(
    additionalStops: List<Pair<String, String>>,
    expanded: Boolean,
    onToggleExpand: () -> Unit
) {
    if (additionalStops.isNotEmpty()) {
        val text =
            if (expanded) "Less stops" else "${additionalStops.size} more stops - click to expand"

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Blue,
            modifier = Modifier
                .clickable(onClick = onToggleExpand)
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
fun RouteDetailCard(routeDetails: RouteDetails) {
    Column {
        routeDetails.stops?.drop(1)?.dropLast(1)?.forEach { stop ->
            StopCard(stop)
        }
    }
}

@Composable
fun StopCard(stop: Stop) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stop.address, style = MaterialTheme.typography.bodyLarge)
        stop.items?.forEach { item ->
            ItemRow(item)
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = "${item.name}: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
    }
}

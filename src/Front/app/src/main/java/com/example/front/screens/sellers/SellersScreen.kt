package com.example.front.screens.sellers

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.front.R
import com.example.front.components.SearchTextField
import com.example.front.components.SmallElipseAndTitle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberMarkerState
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


val location = LatLng(44.015642, 20.912325)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(location, 4f)


@Preview
@Composable
fun SellersScreen() {

    var selectedColumnIndex by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        SmallElipseAndTitle("Shops")
        SearchAndFilters()
        // tabs
        Row(

        )
        {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable { selectedColumnIndex = true }
            )
            {
                Text(text = "Shops", style=MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onTertiary, fontSize = 20.sp),modifier = Modifier.align(Alignment.CenterHorizontally))

                if(selectedColumnIndex)
                {
                    Image(
                        painter = painterResource(id = R.drawable.crtica),
                        contentDescription = "Elipse",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .clickable { selectedColumnIndex = false }
            )
            {
                Text(text = "Your Favorite", style=MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onTertiary, fontSize = 20.sp),modifier = Modifier.align(Alignment.CenterHorizontally))
                if(!selectedColumnIndex)
                {
                    Image(
                        painter = painterResource(id = R.drawable.crtica),
                        contentDescription = "Elipse",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }
        }

        if(selectedColumnIndex)
        {
            AllSellers()
        }
        else{
            FavItems()
        }


    }
}

@Composable
fun FavItems() {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            .padding(top = 20.dp)
    ) {
        Text("Your Favorites", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start=16.dp))
    }
}

@Composable
fun AllSellers() {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            .padding(top = 20.dp)
    ) {
        Text("Explore Seller Locations", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start=16.dp))
        Card(
            modifier = Modifier
                .height(250.dp)
                .padding(16.dp)
        ) {

            Osm()
        }
    }
}

@Composable
fun Osm() {
    val context = LocalContext.current
    val yourUserAgent = "YourUserAgentName"
    Configuration.getInstance().userAgentValue = yourUserAgent
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    AndroidView(
        modifier = Modifier,
        factory = { mapView(context) },
    )
}

private fun mapView(context: Context): MapView {
    val mapView = MapView(context)
    val geoPoint = GeoPoint(44.015889, 20.904429)
    mapView.controller.setZoom(18.0)
    mapView.controller.setCenter(geoPoint)
    mapView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    mapView


    return mapView
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun Mapa(modifier: Modifier, cameraPositionState: CameraPositionState, onMapLoaded: () -> Unit) {
    val locationState = rememberMarkerState(
        position = location
    )

    val mapUiSettings by remember{
        mutableStateOf(MapUiSettings(compassEnabled = false))
    }

    val mapProperties by remember{
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    GoogleMap(
        onMapLoaded = onMapLoaded,
        uiSettings = mapUiSettings,
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ){
        MarkerInfoWindow {

        }

        Marker(
            state = locationState,
            draggable = true,
            onClick = {
                return@Marker false
            },
            title = "Map"
        )
    }
}


@Composable
fun SearchAndFilters() {
    var value by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    )
    {
        //SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it })
        SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it }, modifier = Modifier.fillMaxWidth(0.85f))
        Image(
            // ako budemo imali dark i light ovde mozda neki if i promena slike
            painter = painterResource(id = R.drawable.filters),
            contentDescription = "Search icon",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(40.dp)
                .align(Alignment.CenterVertically),
        )
    }
}



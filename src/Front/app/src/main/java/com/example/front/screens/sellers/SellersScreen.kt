package com.example.front.screens.sellers

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.CardButton
import com.example.front.components.MyTextField
import com.example.front.components.ProductCard
import com.example.front.components.SearchTextField
import com.example.front.components.ShopCard
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.Tabs
import com.example.front.components.ToggleImageButton
import com.example.front.model.user.UserEditDTO
import com.example.front.screens.categories.ClickableCard
import com.example.front.screens.home.CardData
import com.example.front.viewmodels.myprofile.MyProfileViewModel
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


@Composable
fun SellersScreen(navController: NavHostController) {

    var selectedColumnIndex by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        item {
            SmallElipseAndTitle("Shops")
        }
        item {
            SearchAndFilters()
        }
        item{
            Tabs(
                onShopsSelected = { selectedColumnIndex = true },
                onFavoritesSelected = { selectedColumnIndex = false },
                selectedColumnIndex = selectedColumnIndex,
                "Shops",
                "Your Favorites",
                false
            )
        }

        item{
            if(selectedColumnIndex)
            {
                AllSellers(navController)
            }
            else{
                FavItems(navController)
            }
        }


    }
}

data class CardData(
    val id: Int,
    val title: String,
    val description: String,
    val imageResource: Int,
    var isLiked: Boolean
)

@Composable
fun FavItems(navController: NavHostController) {
    val products = listOf(
        CardData(
            id = 1,
            title = "Radionica 'Vlado'",
            description = "Kraljevo 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        ),
        CardData(
            id = 1,
            title = "Vocnjak Miljkovic",
            description = "Address 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        ),
        CardData(
            id = 1,
            title = "Drangulije Ivana",
            description = "Tavnik 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 20.dp)
    ) {
        Text("Your Favorites", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start=16.dp))
        ShopsComponent(products,navController)
    }
}

@Composable
fun AllSellers(navController: NavHostController) {

    val products = listOf(
        CardData(
            id = 1,
            title = "Radionica 'Vlado'",
            description = "Kraljevo 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        ),
        CardData(
            id = 1,
            title = "Vocnjak Miljkovic",
            description = "Address 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        ),
        CardData(
            id = 1,
            title = "Drangulije Ivana",
            description = "Tavnik 123",
            imageResource = R.drawable.imageplaceholder,
            isLiked = false
        )
    )

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 20.dp)
    ) {
        Text("Explore Seller Locations", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start=20.dp))
        Card(
            modifier = Modifier
                .height(250.dp)
                .padding(20.dp)
        ) {

            Osm()
        }
        Text("Uncover Sellers Around You!", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(20.dp,top=0.dp,bottom = 0.dp))
        ShopsComponent(products,navController)
    }
}

@Composable
fun ShopsComponent(products: List<CardData>,navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 600.dp)
        ) {
            items(products) { cardData ->
                ShopCard(
                    title = cardData.title,
                    price = cardData.description,
                    imageResource = cardData.imageResource,
                    navController,
                    cardData.id,
                    "08:00 - 20:00"
                )
            }
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
    var showDialog by remember { mutableStateOf(false) }

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
                .align(Alignment.CenterVertically)
                .clickable { showDialog = true },
        )
    }

    if (showDialog) {
        FiltersDialog(onDismiss = { showDialog = false })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersDialog(onDismiss: () -> Unit) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    var selectedColumnIndex by remember { mutableStateOf(true) }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onDismiss()
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(top = 5.dp)
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            "Cancel", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                                .padding(bottom = 20.dp)
                        )
                        Text(
                            "Filters", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier
                                .padding(bottom = 20.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            "Reset", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary), modifier = Modifier
                                .padding(bottom = 20.dp)
                        )
                    }

                    //tabs
                    Tabs(
                        onShopsSelected = { selectedColumnIndex = true },
                        onFavoritesSelected = { selectedColumnIndex = false },
                        selectedColumnIndex = selectedColumnIndex,
                        "Details",
                        "Location",
                        true
                    )

                    if(selectedColumnIndex)
                    {
                        CardGrid()
                    }
                    else{
                        MapFilters()
                    }

                }

            }
        }
    }
}

@Composable
fun MapFilters() {
    var value by remember { mutableStateOf("") }
    var switchState by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableStateOf(50f) }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it }, modifier = Modifier.fillMaxWidth(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, bottom = 7.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        )
        {
            Text("Near you", modifier = Modifier, style=MaterialTheme.typography.displaySmall)
            Switch(
                checked = switchState,
                onCheckedChange = {
                    switchState = it
                },
                modifier = Modifier
                    .padding(start = 8.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onBackground,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        if(switchState)
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Distance", modifier = Modifier, style=MaterialTheme.typography.displaySmall)
                    Text("0km - "+sliderValue.toInt().toString()+"km", modifier = Modifier, style=MaterialTheme.typography.displaySmall)
                }

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                    },
                    valueRange = 10f..100f,
                    steps = 9,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )
            }
        }

        Card(
            modifier = Modifier
                .height(150.dp)
                .padding(16.dp)
        ) {

            Osm()
        }

        BigBlueButton(text = "Show Results", onClick = { /*TODO*/ }, width = 1f, modifier = Modifier)

    }
}

@Composable
fun CardGrid() {

    val cardData = listOf(
        "Food",
        "Drink",
        "Footwear",
        "Clothes",
        "Jewerly",
        "Tools",
        "Furniture",
        "Pottery",
        "Beauty",
        "Health",
        "Decor",
        "Other",
    )

    Column {
        Text(text = "Categories", modifier = Modifier.padding(top = 16.dp,bottom = 10.dp, start = 10.dp), style=MaterialTheme.typography.displaySmall)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(cardData) { cardText ->
                FilterCard(cardText = cardText, onClick = {})
            }
        }
        Text(text = "Customer Review", modifier = Modifier.padding(top = 16.dp,bottom = 10.dp, start = 10.dp), style=MaterialTheme.typography.displaySmall)
        ReviewStars(brojZvezdica = 4)
        ReviewStars(brojZvezdica = 3)
        ReviewStars(brojZvezdica = 3)
        OpenNow()
        BigBlueButton(text = "Show Results", onClick = { /*TODO*/ }, width = 1f, modifier = Modifier)
    }
}

@Composable
fun OpenNow() {
    var switchState by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, bottom = 7.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text("Open Now", modifier = Modifier, style=MaterialTheme.typography.displaySmall)
        Switch(
            checked = switchState,
            onCheckedChange = {
                switchState = it
            },
            modifier = Modifier
                .padding(start = 8.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onBackground,
                uncheckedThumbColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun ReviewStars(brojZvezdica:Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row()
        {
            repeat(brojZvezdica) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(1.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text("  & up", style = MaterialTheme.typography.titleSmall)
        }

        RadioButton(
            selected = false,
            onClick = { },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun FilterCard(cardText: String, onClick: () -> Unit) {

    val isCardClicked by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(if (isCardClicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            ,
        )
        {
            Text(text = cardText, modifier = Modifier
                .padding(5.dp), style=MaterialTheme.typography.displaySmall.copy(MaterialTheme.colorScheme.background))
        }
    }
}




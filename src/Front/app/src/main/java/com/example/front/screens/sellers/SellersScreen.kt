package com.example.front.screens.sellers

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.front.R
import com.example.front.components.BigBlueButton
import com.example.front.components.Paginator
import com.example.front.components.SearchTextField
import com.example.front.components.ShopCard
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.Tabs
import com.example.front.viewmodels.shops.ShopsViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun SellersScreen(navController: NavHostController, shopsViewModel: ShopsViewModel) {

    var selectedColumnIndex by remember { mutableStateOf(true) }

    var currentPage by remember { mutableStateOf(1) }
    val totalPages = 10

    var currentPageFav by remember { mutableStateOf(1) }
    val totalPagesFav = 10

    LaunchedEffect(Unit) {
        shopsViewModel.getUserId()
            ?.let { shopsViewModel.getShops(it,listOf(),null,false,0,null,0,null,1,false, null, null) }
        shopsViewModel.getUserId()
            ?.let { shopsViewModel.getShops(it,listOf(),null,false,0,null,0,null,1,true, null, null) }
    }

    val x = Osm(shopsViewModel = shopsViewModel)
    shopsViewModel.changeCoordinates(x)

    LazyColumn(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        item {
            SmallElipseAndTitle("Shops")
        }
        item {
            SearchAndFilters(shopsViewModel)
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
                if(shopsViewModel.state.value.error.contains("NotFound"))
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.nofound),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(top = 55.dp, bottom = 16.dp),
                                contentScale = ContentScale.FillWidth
                            )
                            Text("No shops found", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
                else if(shopsViewModel.state.value.isLoading)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(160.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        CircularProgressIndicator()
                    }
                }
                else{
                    AllSellers(navController, shopsViewModel)
                    shopsViewModel.getShopPages()
                    Log.d("SHOP PAGE", shopsViewModel.statePageCount.value.toString())
                    Paginator(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        onPageSelected = { newPage ->
                            if (newPage in 1..totalPages) {
                                currentPage = newPage
                            }
                        }
                    )
                }
            }
            else{
                if(shopsViewModel.stateFav.value.error.contains("NotFound"))
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.nofound),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(top = 55.dp, bottom = 16.dp),
                                contentScale = ContentScale.FillWidth
                            )
                            Text("No shops found", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
                else if(shopsViewModel.stateFav.value.isLoading)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(160.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        CircularProgressIndicator()
                    }
                }
                else{
                    FavItems(navController, shopsViewModel)
                    Paginator(
                        currentPage = currentPageFav,
                        totalPages = totalPagesFav,
                        onPageSelected = { newPage ->
                            if (newPage in 1..totalPagesFav) {
                                currentPageFav = newPage
                            }
                        }
                    )
                }
            }
        }
    }
}

data class DataCard(
    val id: Int,
    val title: String,
    val description: String,
    val imageResource: String,
    var isLiked: Boolean,
    var rating: Float
)

@Composable
fun FavItems(navController: NavHostController, shopsViewModel: ShopsViewModel) {
    val state = shopsViewModel.stateFav.value
    val shops = state.shops?.mapIndexed { index, productsState ->
        DataCard(
            id = productsState.id,
            title = productsState.name,
            description = productsState.address.toString(),
            imageResource = productsState.image.toString(),
            isLiked = false,
            rating = productsState.rating
        )
    }?.toList() ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 20.dp)
    ) {
        Text("Your Favorites", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(start=16.dp))
        ShopsComponent(shops,navController)
    }
}

@Composable
fun AllSellers(navController: NavHostController, shopsViewModel: ShopsViewModel) {

    val state = shopsViewModel.state.value
    val shops = state.shops?.mapIndexed { index, productsState ->
        DataCard(
            id = productsState.id,
            title = productsState.name,
            description = productsState.address,
            imageResource = productsState.image.toString(),
            isLiked = false,
            rating = productsState.rating
        )
    }?.toList() ?: emptyList()

    val coordinates = mutableListOf<GeoPoint>()
    for(shop in state.shops!!)
    {
        coordinates.add(GeoPoint(shop.latitude.toDouble(),shop.longitude.toDouble()))
    }


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

            Osm(shopsViewModel)
        }
        Text("Uncover Sellers Around You!", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(20.dp,top=0.dp,bottom = 0.dp))
        ShopsComponent(shops,navController)
    }
}

@Composable
fun ShopsComponent(products: List<DataCard>, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(100.dp, 600.dp)
        ) {
            items(products) { cardData ->
                var rating: String = "Rate this store first!"
                if(cardData.rating > 0f)
                {
                    rating = cardData.rating.toString()
                }
                ShopCard(
                    title = cardData.title,
                    price = cardData.description,
                    imageResource = cardData.imageResource,
                    navController,
                    cardData.id,
                    rating
                )
            }
        }
    }
}

@Composable
fun SearchAndFilters(shopsViewModel: ShopsViewModel) {
    var value by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isOverlayVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    )
    {
        SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it; shopsViewModel.Search(value) }, modifier = Modifier.fillMaxWidth(0.75f))
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
        Image(
            // ako budemo imali dark i light ovde mozda neki if i promena slike
            painter = painterResource(id = R.drawable.sort),
            contentDescription = "Sort icon",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(50.dp, 40.dp)
                .align(Alignment.CenterVertically)
                .clickable { isOverlayVisible = true },
        )
    }

    if (showDialog) {
        FiltersDialog(onDismiss = { showDialog = false }, shopsViewModel)
    }
    if (isOverlayVisible) {
        Overlay(onDismiss = { isOverlayVisible = false}, shopsViewModel)
    }
}

@Composable
fun Overlay(onDismiss: () -> Unit, shopsViewModel:ShopsViewModel) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    var value by remember { mutableStateOf(0) }

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
                    .align(Alignment.Center),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        "Sort", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier
                            .padding(bottom = 40.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        "Default", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(0) }
                    )
                    Text(
                        "Rating (lowest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(1) }
                    )
                    Text(
                        "Rating (highest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(2) }
                    )
                    Text(
                        "Alphabetically (ascending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(3) }
                    )
                    Text(
                        "Alphabetically (descending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(4) }
                    )

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersDialog(onDismiss: () -> Unit, shopsViewModel: ShopsViewModel) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    var selectedColumnIndex by remember { mutableStateOf(true) }

    var selectedCategories by remember { mutableStateOf(shopsViewModel.filtersState.value.categories!!.toMutableList()) }
    var review by remember { mutableStateOf(shopsViewModel.filtersState.value.rating) }
    var open by remember { mutableStateOf(if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false ) }
    var location by remember { mutableStateOf(if(shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()) }
    var range by remember {mutableStateOf(if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f)}

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        shopsViewModel.changeCategories(selectedCategories)
                        shopsViewModel.changeRating(review)
                        shopsViewModel.changeOpen(open)
                        shopsViewModel.changeLocation(location)
                        shopsViewModel.changeRange(range.toInt())
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
                                .clickable {
                                    shopsViewModel.changeCategories(selectedCategories)
                                    shopsViewModel.changeRating(review)
                                    shopsViewModel.changeOpen(open)
                                    shopsViewModel.changeLocation(location)
                                    shopsViewModel.changeRange(range.toInt())
                                    onDismiss()
                                }
                        )
                        Text(
                            "Filters", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground), modifier = Modifier
                                .padding(bottom = 20.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            "Reset", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary), modifier = Modifier
                                .padding(bottom = 20.dp)
                                .clickable {
                                    shopsViewModel.changeCategories(selectedCategories)
                                    shopsViewModel.changeRating(review)
                                    shopsViewModel.changeOpen(open)
                                    shopsViewModel.changeLocation(location)
                                    shopsViewModel.changeRange(range.toInt())
                                    shopsViewModel.withoutFilters()
                                }
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
                        CardGrid(onCategorySelected={selectedCategories=it},
                        onReviewSelected={review=it},
                        onOpenChanged={open=it}, shopsViewModel)
                    }
                    else{
                        MapFilters(onSearchChange={location=it},onSliderChange={range=it},shopsViewModel)
                    }
                    BigBlueButton(text = "Show Results", onClick = {
                        shopsViewModel.DialogFilters(selectedCategories,review,open,location,range.toInt())
                        onDismiss()
                    }, width = 1f, modifier = Modifier)
                }
            }
        }
    }
}


@Composable
fun MapFilters(
    onSearchChange: (String) -> Unit,
    onSliderChange: (Float) -> Unit,
    shopsViewModel: ShopsViewModel
){
    var value by remember { mutableStateOf(if(shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()) }
    var switchState by remember { mutableStateOf(shopsViewModel.filtersState.value.range != 0) }
    var sliderValue by remember { mutableStateOf(if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f) }

    LaunchedEffect(shopsViewModel.filtersState.value) {
        val filtersState = shopsViewModel.filtersState.value

        val location = filtersState.location
        val switch = shopsViewModel.filtersState.value.range != 0
        val slider =if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f

        value = location!!
        switchState = switch
        sliderValue = slider

    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchTextField(valuee = value, placeh = "Search sellers", onValueChangee = { value = it; onSearchChange(value) }, modifier = Modifier.fillMaxWidth(1f))
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
                    if(!switchState)
                    {
                        sliderValue = 0f;
                        onSliderChange(0f)
                    }
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
                        onSliderChange(sliderValue)
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

            Osm(shopsViewModel)
        }

    }
}


@Composable
fun CardGrid(
    onCategorySelected: (MutableList<Int>) -> Unit,
    onReviewSelected: (Int) -> Unit,
    onOpenChanged: (Boolean) -> Unit,
    shopsViewModel: ShopsViewModel
){
    var selectedCategories by remember { mutableStateOf(shopsViewModel.filtersState.value.categories!!.toMutableList()) }
    var radio = shopsViewModel.filtersState.value.rating
    var radiobutton1 by remember { mutableStateOf(!(radio == null || radio!=4)) }
    var radiobutton2 by remember { mutableStateOf(!(radio ==null || radio!=3)) }
    var radiobutton3 by remember { mutableStateOf(!(radio ==null || radio!=2)) }
    var switchState by remember { mutableStateOf(if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false ) }
    var indikator by remember { mutableStateOf(0) }

    val cardData = listOf(
        "Food", "Drink", "Footwear", "Clothes", "Jewerly", "Tools", "Furniture", "Pottery", "Beauty", "Health", "Decor", "Other"
    )

    var brojevi = (1..12).map { it }
    var kombinovanaLista = cardData.zip(brojevi)


    LaunchedEffect(shopsViewModel.filtersState.value) {
        val filtersState = shopsViewModel.filtersState.value

        val categ = filtersState.categories!!.toMutableList()
        val newRadio = filtersState.rating
        val newSwitchState = if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false

        selectedCategories = categ
        indikator+=14
        radio = newRadio
        radiobutton1 = !(newRadio == null || newRadio != 4)
        radiobutton2 = !(newRadio == null || newRadio != 3)
        radiobutton3 = !(newRadio == null || newRadio != 2)
        switchState = newSwitchState
    }


    Column {
        Text(text = "Categories", modifier = Modifier.padding(top = 16.dp,bottom = 10.dp, start = 10.dp), style=MaterialTheme.typography.displaySmall)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(kombinovanaLista,key={(_, number) -> number+indikator }) { (cardText,number) ->
                FilterCard(cardText = cardText, onClick = {
                    if (selectedCategories.contains(number)) {
                        selectedCategories.remove(number)
                        onCategorySelected(selectedCategories)
                    } else {
                        selectedCategories.add(number)
                        onCategorySelected(selectedCategories)
                    }
                },selectedCategories.contains(number) )
            }
        }
        Text(text = "Customer Review", modifier = Modifier.padding(top = 16.dp,bottom = 10.dp, start = 10.dp), style=MaterialTheme.typography.displaySmall)
        ReviewStars(brojZvezdica = 4, onClick={radiobutton1=true; radiobutton2=false; radiobutton3 =false; onReviewSelected(4)}, radiobutton1)
        ReviewStars(brojZvezdica = 3, onClick={radiobutton1=false; radiobutton2=true; radiobutton3 =false; onReviewSelected(3)}, radiobutton2)
        ReviewStars(brojZvezdica = 2, onClick={radiobutton1=false; radiobutton2=false; radiobutton3 =true;onReviewSelected(2)}, radiobutton3)
        OpenNow(switchState!!, onCheckedChange={
            switchState = !switchState!!
            onOpenChanged(switchState!!)
        })
    }

}

@Composable
fun OpenNow(switchState:Boolean, onCheckedChange : () -> Unit) {

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
                onCheckedChange()
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
fun ReviewStars(brojZvezdica:Int, onClick: () -> Unit, isClicked:Boolean) {
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
            selected = isClicked,
            onClick = {
                onClick()
            },
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
fun FilterCard(cardText: String, onClick: () -> Unit, isClicked: Boolean) {

    var isCardClicked by remember {
        mutableStateOf(isClicked)
    }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                isCardClicked = !isCardClicked
                onClick()
            }
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




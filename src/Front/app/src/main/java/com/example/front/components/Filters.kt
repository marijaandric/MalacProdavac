package com.example.front.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.front.screens.sellers.FilterCard
import com.example.front.screens.sellers.ReviewStars
import com.example.front.viewmodels.oneshop.OneShopViewModel
import com.example.front.viewmodels.shops.ShopsViewModel

@Composable
fun FilterDialogProducts(onDismiss: () -> Unit, shopsViewModel: OneShopViewModel, shopId:Int) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    var selectedColumnIndex by remember { mutableStateOf(true) }

    var selectedCategories by remember { mutableStateOf(shopsViewModel.filtersState.value.categories!!.toMutableList()) }
    var review by remember { mutableStateOf(shopsViewModel.filtersState.value.rating) }
    var open by remember { mutableStateOf(if(shopsViewModel.filtersState.value.open != null) shopsViewModel.filtersState.value.open else null ) }
    var location by remember { mutableStateOf(if(shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()) }
    var range by remember { mutableStateOf(if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f) }

    LaunchedEffect(shopsViewModel.filtersState.value)
    {
        selectedCategories = shopsViewModel.filtersState.value.categories!!.toMutableList()
        review = shopsViewModel.filtersState.value.rating
        open = if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false
        location = if(shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()
        range = if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f
    }

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
//                        shopsViewModel.changeLocation(location)
//                        shopsViewModel.changeRange(range.toInt())
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
//                                    shopsViewModel.changeLocation(location)
//                                    shopsViewModel.changeRange(range.toInt())
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
//                                    shopsViewModel.changeLocation(location)
//                                    shopsViewModel.changeRange(range.toInt())
                                    shopsViewModel.withoutFilters()
                                }
                        )
                    }

                    //tabs
                    if(shopId == 0 || shopId == null) {
                        Tabs(
                            onShopsSelected = { selectedColumnIndex = true },
                            onFavoritesSelected = { selectedColumnIndex = false },
                            selectedColumnIndex = selectedColumnIndex,
                            "Details",
                            "Location",
                            true
                        )
                    }

                    if(selectedColumnIndex)
                    {
                        CardGridForProduct(onCategorySelected={selectedCategories=it},
                            onReviewSelected={review=it},
                            onOpenChanged={open=it}, shopsViewModel)
                    }
                    else if(shopId != 0 && shopId != null){
                        MapFiltersForProduct(onSearchChange={/*location=it*/},onSliderChange={/*range=it*/},shopsViewModel)
                    }
                    BigBlueButton(text = "Show Results", onClick = {
                        shopsViewModel.DialogFilters(selectedCategories,review,open,location,range.toInt(), shopId)
                        onDismiss()
                    }, width = 1f, modifier = Modifier)
                }
            }
        }
    }
}


@Composable
fun MapFiltersForProduct(
    onSearchChange: (String) -> Unit,
    onSliderChange: (Float) -> Unit,
    shopsViewModel: OneShopViewModel
){
    var value by remember { mutableStateOf(if(shopsViewModel.filtersState.value.location == null) "" else shopsViewModel.filtersState.value.location.toString()) }
    var switchState by remember { mutableStateOf(shopsViewModel.filtersState.value.range != 0) }
    var sliderValue by remember { mutableStateOf(if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f) }

    LaunchedEffect(shopsViewModel.filtersState.value) {
        val filtersState = shopsViewModel.filtersState.value

        val location = filtersState.location
        val switch = if(shopsViewModel.filtersState.value.range == null)false else if(shopsViewModel.filtersState.value.range != 0) true else false
        val slider =if(shopsViewModel.filtersState.value.range != null) shopsViewModel.filtersState.value.range!!.toFloat() else 0f

        value = if(location == null) "" else location.toString()
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

            //Osm(shopsViewModel)
        }

    }
}


@Composable
fun CardGridForProduct(
    onCategorySelected: (MutableList<Int>) -> Unit,
    onReviewSelected: (Int) -> Unit,
    onOpenChanged: (Boolean) -> Unit,
    shopsViewModel: OneShopViewModel
){
    var selectedCategories by remember { mutableStateOf(shopsViewModel.filtersState.value.categories!!.toMutableList()) }
    var radio = shopsViewModel.filtersState.value.rating
    var radiobutton1 by remember { mutableStateOf(!(radio == null || radio!=4)) }
    var radiobutton2 by remember { mutableStateOf(!(radio ==null || radio!=3)) }
    var radiobutton3 by remember { mutableStateOf(!(radio ==null || radio!=2)) }
    var switchState by remember { mutableStateOf(if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false ) }

    val cardData = listOf(
        "Food", "Drink", "Footwear", "Clothes", "Jewerly", "Tools", "Furniture", "Pottery", "Beauty", "Health", "Decor", "Other"
    )

    var brojevi = (1..12).map { it }
    var kombinovanaLista = cardData.zip(brojevi)
    var indikator by remember { mutableStateOf(0) }


    LaunchedEffect(shopsViewModel.filtersState.value) {
        val filtersState = shopsViewModel.filtersState.value
//
        val categ = filtersState.categories!!.toMutableList()
        val newRadio = filtersState.rating
        val newSwitchState = if(shopsViewModel.filtersState.value.open != null)shopsViewModel.filtersState.value.open else false
//
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
                }, selectedCategories.contains(number))
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
fun SortDialog(onDismiss: () -> Unit, shopsViewModel: OneShopViewModel, shopId: Int?) {
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
                            .clickable { shopsViewModel.Sort(0,shopId); onDismiss() }
                    )
                    Text(
                        "Rating (lowest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(1,shopId); onDismiss() }
                    )
                    Text(
                        "Rating (highest first)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(2,shopId); onDismiss() }
                    )
                    Text(
                        "Alphabetically (ascending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(3,shopId); onDismiss() }
                    )
                    Text(
                        "Alphabetically (descending)", style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary), modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable { shopsViewModel.Sort(4,shopId) ; onDismiss()}
                    )
                }
            }
        }
    }
}


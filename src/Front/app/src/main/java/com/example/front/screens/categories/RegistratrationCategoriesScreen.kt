package com.example.front.screens.categories

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.components.MediumBlueButton
import com.example.front.R
import com.example.front.repository.Repository
import com.example.front.viewmodels.categories.CategoriesViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistrationCategories() {

    var viewModel: CategoriesViewModel
    val repository = Repository()
    viewModel = CategoriesViewModel(repository)
    viewModel.getCategoriesInfo()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Title()
            Spacer(modifier = Modifier.height(10.dp))
        }
        item{
            //FlowRow {
                Cards(viewModel)
            //}
        }
    }
}

@Composable
fun Title() {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.ellipsebrojdva),
            contentDescription = "Elipse",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Choose at least 2 categories:",
            fontSize = 25.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 50.dp, start = 25.dp, end = 25.dp, bottom = 15.dp),
            color = Color.White

        )
    }
}

data class CardData(val image: Int, val description: String, val idCat : Int)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cards(viewModel: CategoriesViewModel) {

    val progressAnimation by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing), label = ""
    )

    val state = viewModel.state.value
    if(state.isLoading)
    {
        Spacer(modifier = Modifier.height(40.dp))

        CircularProgressIndicator(
            modifier = Modifier
                .size(70.dp)
                .padding(8.dp),
            strokeWidth = 4.dp,
            progress = progressAnimation
        )

    }// ne bi bilo lose dodati ekran za prikaz greske
    else
    {
        val icons = listOf(
            R.drawable.foodicon,
            R.drawable.drinkicon,
            R.drawable.toolsicon,
            R.drawable.clothesicon,
            R.drawable.necklaceicon,
            R.drawable.footwearicon,
            R.drawable.furnitureicon,
            R.drawable.potteryicon,
            R.drawable.cosmeticsicon,
            R.drawable.articon,
            R.drawable.healthicon,
            R.drawable.dotsicon
        )
        Log.d("Taaaaaaaaaag", state.categories.toString())
        val cardData = state.categories?.mapIndexed { index, categoriesState ->
            CardData(
                image = icons.get(index),
                description = categoriesState.name,
                idCat = categoriesState.id
            )
        }?.toList() ?: emptyList()

        LazyVerticalGrid (columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 25.dp)
                .heightIn(400.dp, 600.dp)
        ) {
            items(cardData) { card ->
                ClickableCard(
                    image = card.image,
                    description = card.description,
                    onClick = {

                    }
                )
            }
        }


        Box(
            contentAlignment = Alignment.Center
        )
        {
            Spacer(modifier = Modifier.height(20.dp))
            MediumBlueButton(text = "Continue", onClick = { /*TODO*/ }, width = 0.90f, modifier = Modifier)
        }


    }



}

@Composable
fun ClickableCard(
    image: Int,
    description: String,
    onClick: () -> Unit
) {
    var isCardClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(20),
            modifier = Modifier
                .padding(2.dp)
                .clickable {
                    isCardClicked = !isCardClicked
                }
                .height(80.dp)
                .width(80.dp)
                //
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isCardClicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                ,
            )
            {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Food Icon",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        Text(
            text = description,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

}


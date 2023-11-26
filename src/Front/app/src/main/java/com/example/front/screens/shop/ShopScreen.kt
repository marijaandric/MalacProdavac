package com.example.front.screens.shop

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.front.R
import com.example.front.components.ButtonWithIcon
import com.example.front.components.CardButton
import com.example.front.components.CommentsTextBox
import com.example.front.components.FilterDialogProducts
import com.example.front.components.MyTextField
import com.example.front.components.MyTextFieldWithoutIcon
import com.example.front.components.ReviewCard
import com.example.front.components.SearchTextField
import com.example.front.components.ShopProductCard
import com.example.front.components.SmallElipseAndTitle
import com.example.front.components.SortDialog
import com.example.front.components.ToggleImageButton
import com.example.front.components.ToggleImageButtonFunction
import com.example.front.screens.categories.ClickableCard
import com.example.front.screens.sellers.FiltersDialog
import com.example.front.viewmodels.oneshop.OneShopViewModel
import com.example.front.viewmodels.shops.ShopsViewModel
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun ShopScreen(navController: NavHostController, shopViewModel: OneShopViewModel, shopId: Int) {
    var selectedColumnIndex by remember { mutableStateOf(true) }
    var id by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        shopViewModel.getUserId()
            ?.let {
                shopViewModel.getShopDetails(it, shopId)
                id = it
            }
        shopViewModel.getUserId()
            ?.let {
                shopViewModel.getProducts(it, listOf(),null,null,null,null,0,null,1,shopId, false,null,null )
            }
        shopViewModel.getShopReview(shopId,0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    )
    {
        item{
            SmallElipseAndTitle(title = "Shop")
        }
        if(shopViewModel.state.value.isLoading)
        {
            item{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator()
                }
            }
        }
        else{
            item{
                //shop for user
                ProfilePic(shopViewModel,id)
            }
            item{
                ShopInfo(shopViewModel, shopId, id)
            }
        }

    }
}


@Composable
fun ShopInfo(shopViewModel: OneShopViewModel, shopId:Int, userID:Int) {
    var isImageClicked by remember { mutableStateOf(true) }
    var shopReviewsPage by remember { mutableStateOf(0f) }

    val scaleImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.1.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val scaleImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 1.1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage1 by animateDpAsState(
        targetValue = if (isImageClicked) 1.dp else 0.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val zIndexImage2 by animateDpAsState(
        targetValue = if (isImageClicked) 0.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.jakoprovidnaelipsa),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage1.value)
                    .graphicsLayer(scaleX = scaleImage1.value, scaleY = scaleImage1.value)
            )
            Image(
                painter = painterResource(id = R.drawable.jakoprovidnaelipsa2),
                contentDescription = "Elipse",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(zIndexImage2.value)
                    .graphicsLayer(scaleX = scaleImage2.value, scaleY = scaleImage2.value)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 15.dp, end = 16.dp)
                    .zIndex(2f)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(2f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Info",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(isImageClicked) 25.sp else 20.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = true
                        }
                    )
                    Text(
                        "Products",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = if(!isImageClicked) 25.sp else 20.sp, fontWeight = FontWeight.Medium,color=MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.clickable {
                            isImageClicked = false
                        }
                    )
                }

                Info(isImageClicked,shopViewModel,shopId, userID, LocalContext.current)
                Products(isImageClicked,shopViewModel, shopId)

            }
        }
    }
}

@Composable
fun Products(isImageClicked: Boolean,shopViewModel: OneShopViewModel,shopId:Int) {
    var showElseText by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showAddProduct by remember { mutableStateOf(false) }
    var showSortDialog by remember {
        mutableStateOf(false)
    }

    var value by remember {
        mutableStateOf("")
    }

    val data = listOf(
        1,2,3,4
    )

    LaunchedEffect(!isImageClicked) {
        delay(200)
        showElseText = true
    }
    if(showElseText && !isImageClicked) {
        Column {
            if(!shopViewModel.state.value.shop!!.isOwner)
            {
                Column(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    ButtonWithIcon(text = "Add product", onClick = { showAddProduct = true }, width = 0.6f, color = MaterialTheme.colorScheme.primary, imagePainter = painterResource(
                        id = R.drawable.plus,
                    ), height = 40)
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                SearchTextField(valuee = value, placeh = "Search products", onValueChangee = {value = it; shopViewModel.Search(value, shopId)}, modifier = Modifier.fillMaxWidth(0.65f))
                Image(
                    painter = painterResource(id = R.drawable.filters),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                        .clickable {
                            showDialog = true
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                        .clickable { showSortDialog = true }
                )
            }
            if(shopViewModel.stateProduct.value.isLoading)
            {
                CircularProgressIndicator()
            }
            else if(shopViewModel.stateProduct.value.error.contains("NotFound"))
            {
                androidx.compose.material3.Text(
                    "No products found",
                    style = MaterialTheme.typography.titleSmall, modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            else{
                LazyVerticalGrid (columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 25.dp)
                        .heightIn(400.dp, 600.dp)
                ) {
                    shopViewModel.stateProduct.value.products?.let { products ->
                        items(products) { product ->
                            ShopProductCard(
                                imageRes = product.image,
                                text = product.name,
                                price = "${product.price} din/kom",
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }

        }
    }
    if (showDialog) {
        FilterDialogProducts(onDismiss = { showDialog = false }, shopViewModel, shopId)
    }
    if (showSortDialog) {
        SortDialog(onDismiss = { showSortDialog = false }, shopViewModel, shopId)
    }
    //showAddProduct
    if(showAddProduct)
    {
        AddProductDialog(onDismiss = {showAddProduct = false}, shopViewModel, shopId)
    }
}


@Composable
fun AddProductDialog(onDismiss: () -> Unit, shopsViewModel: OneShopViewModel, shopId: Int?) {
    val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

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
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                        }
                    }
                    .padding(top = 5.dp)
                    .align(Alignment.Center),
            ) {
            }
        }
    }
}

@Composable
@Preview
fun contentOfAddNewProduct(){
    Column(
    ){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.upload_image),
                contentDescription = "Placeholder",
                modifier = Modifier
                    .size(150.dp)
                    .padding(20.dp),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center
            )
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
        ){
            item{
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        "Add new product",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                    )
                }
            }
            item{
                Box(modifier = Modifier.padding(start=16.dp,end=10.dp))
                {
                    MyTextFieldWithoutIcon(labelValue = "Product name", value = "", onValueChange = {}, modifier = Modifier)
                }
            }
            item{
                CommentsTextBox(onReviewTextChanged = {}, "Product Description")
            }
            item{
                Box(modifier = Modifier.padding(start=16.dp,end=16.dp))
                {
                    MyTextFieldWithoutIcon(
                        labelValue = "Quantity in stock",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth())
                }
            }

            item{
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    MyTextFieldWithoutIcon(
                        labelValue = "Unity price",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 2.dp) // Add padding to the end of the first TextField
                    )

                    MyTextFieldWithoutIcon(
                        labelValue = "Unity of quantity",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 2.dp) // Add padding to the start of the second TextField
                    )
                }

            }

        }
    }
}

@Composable
fun Info(isImageClicked: Boolean, shopViewModel: OneShopViewModel, shopId: Int, userID:Int, context: Context) {
    val state = shopViewModel.state.value.shop
    var showText by remember { mutableStateOf(false) }
    var firstTime by remember { mutableStateOf(true) }
    var showReviews by remember { mutableStateOf(false) }
    var leaveAReview by remember { mutableStateOf(false) }
    var reviewPage by remember { mutableStateOf(0) }
    var comment by remember {
        mutableStateOf("")
    }
    var selectedRating by remember { mutableStateOf(0) }
    var toast by remember { mutableStateOf(false) }


    if(isImageClicked) {
        var showText by remember { mutableStateOf(false) }

        LaunchedEffect(isImageClicked) {
            delay(200)
            showText = true
            firstTime = false
        }
        if(showText || firstTime) {

            Column(
                modifier = Modifier
                    .padding(top = 50.dp, start = 5.dp)
                    .zIndex(2f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -- SHOP CATEGORIES
                    Image(
                        painter = painterResource(id = R.drawable.categories),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Shop categories",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }
                Text(
                    text = state!!.categories.joinToString(", "),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )


                // -- SUBCATEGORIES --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.subcategories),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Shop subcategories",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )

                }

                if(state!!.subcategories!!.isEmpty())
                {
                    Text(
                        text = "-",
                        modifier = Modifier.padding(top = 8.dp),
                        style =MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
                else{
                    Text(
                        text = state!!.subcategories!!.joinToString(", "),
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }


                // -- PICK UP TIME --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Placeholder",
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = "Pick up time",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }

                val workingHours = shopViewModel.state.value.shop!!.workingHours

                if (workingHours != null) {
                    for (workingHour in workingHours) {
                        val dayOfWeek = when (workingHour.day) {
                            1 -> "Mon"
                            2 -> "Tue"
                            3 -> "Wen"
                            4 -> "Tru"
                            5 -> "Fri"
                            6 -> "Sut"
                            7 -> "Sun"
                            else -> {"-"}
                        }
                        val openingHours = workingHour.openingHours
                        val closingHours = workingHour.closingHours
                        Text(
                        text = "$dayOfWeek: $openingHours - $closingHours",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }

                // -- REVIEWS --
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Search icon",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Reviews",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color=MaterialTheme.colorScheme.onSurface)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "Leave a review",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                    Icon(
                        imageVector = if(leaveAReview) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                leaveAReview = !leaveAReview
                            }
                            .padding(top = 8.dp)
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                if(leaveAReview)
                {
                    StarRating{rating ->
                    selectedRating = rating}
                    CommentsTextBox(onReviewTextChanged={newText -> comment = newText}, placeholder = "Leave a review")
                    CardButton("Submit review",onClick={
                        shopViewModel.leaveReview(shopId,userID,selectedRating, comment)
                        toast = true
                    },0.9f,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(50.dp), MaterialTheme.colorScheme.secondary)
                    if(!shopViewModel.statePostReview.value.isLoading && toast)
                    {
                        Toast.makeText(context,"Review successfully submitted", Toast.LENGTH_LONG).show()
                        shopViewModel.getShopReview(
                            shopId,
                            reviewPage
                        )
                        toast = false
                    }
                    else if(shopViewModel.statePostReview.value.error.isNotEmpty() && toast){
                        Toast.makeText(context,"Error", Toast.LENGTH_LONG).show()
                        toast = false
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "View all reviews",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                    Icon(
                        imageVector = if(showReviews)Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                showReviews = !showReviews
                            }
                            .padding(top = 8.dp)
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                if(showReviews)
                {
                    val rev= shopViewModel.stateReview.value.reviews
                    if(rev.isEmpty())
                    {
                        Text("No reviews found for this store.", style= MaterialTheme.typography.titleSmall ,modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp))
                    }
                    LazyColumn(
                        modifier = Modifier.heightIn(100.dp, 1000.dp)
                    ) {
                        items(rev) { review ->
                            ReviewCard(
                                username = review.username,
                                imageRes = review.image,
                                comment = review.comment,
                                rating = review.rating
                            )
                        }
                    }
                    if(!shopViewModel.stateReview.value.error.contains("NotFound"))
                    {
                        Text("See more", style= MaterialTheme.typography.titleSmall ,modifier = Modifier
                            .clickable {
                                reviewPage++; shopViewModel.getShopReview(
                                shopId,
                                reviewPage
                            )
                            }
                            .align(Alignment.CenterHorizontally))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfilePic(shopViewModel: OneShopViewModel,id: Int) {
    val state = shopViewModel.state.value
    Box(
        modifier = Modifier
            .padding(top = 50.dp, end = 16.dp, start = 16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        if(!shopViewModel.state.value.shop!!.isOwner)
        {
            ToggleImageButtonFunction(modifier = Modifier.align(Alignment.TopEnd), onClick={
                shopViewModel.changeToggleLike(id, shopViewModel.state.value.shop!!.id)
            })
        }
        else{
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Search icon",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clickable {

                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }


        Image(
            painter = painterResource(id = if(shopViewModel.state.value.shop!!.isOwner) R.drawable.addshop else R.drawable.navbar_message),
            contentDescription = "Search icon",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {

                },
        )

        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = "Search icon",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {

                },
            tint = MaterialTheme.colorScheme.primary
        )

        if(shopViewModel.state.value.shop!!.image == null)
        {
            Image(
                painter = painterResource(id = R.drawable.imageplaceholder),
                contentDescription = "Placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )
        }
        else
        {
            val image = shopViewModel.state.value.shop!!.image
            val imageUrl = "http://softeng.pmf.kg.ac.rs:10015/images/${image}"

            val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
            Image(
                painter = painter,
                contentDescription = "Placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )
        }
    }
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.shop!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = state.shop!!.address, style = MaterialTheme.typography.titleSmall,color = MaterialTheme.colorScheme.primary)
            RatingBar(rating = shopViewModel.state.value.shop!!.rating!!.toFloat())
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


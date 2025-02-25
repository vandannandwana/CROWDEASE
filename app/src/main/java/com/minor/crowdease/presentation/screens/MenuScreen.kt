package com.minor.crowdease.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val selectedItems = remember {
        mutableStateMapOf<Product, Int>()
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    var totalAmountOfSelectedItems by remember {
        mutableStateOf(0)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(selectedItems.toList()) {
        val items = selectedItems.toList()
        totalAmountOfSelectedItems = items.sumOf { it.first.price * it.second }
        if(selectedItems.isEmpty()){
            scope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }
    }

    Scaffold {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),

            sheetContent = {
                LazyColumn(verticalArrangement = Arrangement.Bottom) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Item",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(0.3f)
                            )
                            Text(
                                text = "Quantity",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f)
                            )
                            Text(
                                text = "Price",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f)
                            )
                            Text(
                                text = "Total",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f)
                            )
                        }
                    }
                    items(selectedItems.toList()) { item ->
                        BillItemView(item = item, selectedItems = selectedItems)
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Constants.PAYMENT_GREEN_COLOR)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${selectedItems.size} | items $ $totalAmountOfSelectedItems",
                                fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                                color = Color.White
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Pay",
                                    fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.Paid,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            },
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(modifier = Modifier) {
                                Text(
                                    "UR  CHOICE",
                                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                                    color = Constants.BLUE_COLOR,
                                    fontSize = 32.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = "notification_icon"
                                )

                            }

                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 7.dp
                            )
                        ) {}
                    }

                    item {
                        //SearchBox
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(18.dp)),
                            value = searchQuery,
                            textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
                            placeholder = {
                                Text(
                                    "Search Your Favourite Item...",
                                    fontFamily = Constants.POOPINS_FONT_REGULAR
                                )
                            },
                            onValueChange = { searchQuery = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    tint = Color.LightGray,
                                    contentDescription = "search_icn",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(start = 2.dp)
                                )
                            }
                        )
                    }

                    items(products) { product ->

                        ProductItem(
                            product = product,
                            selectedItems = selectedItems,
                            scaffoldState = scaffoldState,
                            scope = scope
                        )

                    }


                }

            }

        }

    }

}


data class Product(
    val name: String,
    val description: String,
    val price: Int,
    val rating: String,
    val selected: Boolean,
    val image: Int
)

val products = listOf(
    Product(
        name = "Mayo Cheese Burger",
        description = "Veg patty, Cheese, Mayonnaise,vegetables and sauces",
        price = 60,
        rating = "4.5",
        selected = false,
        image = R.drawable.burger_image
    ),
    Product(
        name = "Veg Cheese Puff",
        description = "Veg patty, Cheese, Mayonnaise,vegetables and sauces",
        price = 90,
        rating = "4.2",
        selected = false,
        image = R.drawable.puff_image
    ),
    Product(
        name = "Mayo Cheeze Pizza",
        description = "Veg patty, Vegetables, Mayonnaise,vegetables and sauces",
        price = 90,
        rating = "4.0",
        selected = false,
        image = R.drawable.pizza_image
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductItem(
    product: Product,
    selectedItems: MutableMap<Product, Int>,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                //Product Image

                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .height(140.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(product.image),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(7.dp))
                    )
                }

                //Product Info
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(140.dp)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            product.name,
                            fontFamily = Constants.POOPINS_FONT_BOLD
                        )
                        Text(
                            product.description,
                            fontSize = 10.sp,
                            fontFamily = Constants.POOPINS_FONT_REGULAR
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "$ ${product.price}",
                            color = Constants.BLUE_COLOR,
                            fontFamily = Constants.POOPINS_FONT_BOLD
                        )
                        Icon(
                            imageVector = Icons.Default.RemoveCircle,
                            contentDescription = "remove_item",
                            modifier = Modifier.clickable {
                                if (selectedItems.containsKey(product)) {

                                    if(selectedItems.isEmpty()){
                                        scope.launch {
                                            scaffoldState.bottomSheetState.hide()
                                        }
                                    }

                                    if (selectedItems[product] == 1) {
                                        selectedItems.remove(product)
                                    } else {
                                        selectedItems[product] = selectedItems[product]!!.minus(1)
                                    }
                                }
                            }
                        )
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "add_item",
                            modifier = Modifier.clickable {
                                if (selectedItems.containsKey(product)) {
                                    selectedItems[product] = selectedItems[product]!!.plus(1)
                                } else {
                                    selectedItems[product] = 1
                                }
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.StarRate,
                        contentDescription = "",
                        tint = Color.Green,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(product.rating)
                }


            }

            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Constants.BLUE_COLOR)
                    .padding(horizontal = 24.dp, vertical = 2.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {

                        if (selectedItems.containsKey(product)) {
                            selectedItems[product] = selectedItems[product]!!.plus(1)
                        } else {
                            selectedItems[product] = 1
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Add",
                    color = Color.White,
                    fontFamily = Constants.POOPINS_FONT_REGULAR
                )
            }

        }

    }

}


@Composable
fun BillItemView(
    item: Pair<Product, Int>,
    modifier: Modifier = Modifier,
    selectedItems: MutableMap<Product, Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.first.name,
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = "${item.second}",
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "$ ${item.first.price}",
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
        val total_price = item.first.price * item.second
        Text(
            text = "$ ${total_price}",
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f)
        )
    }
}


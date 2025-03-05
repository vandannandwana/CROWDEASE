package com.minor.crowdease.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.data.dto.food_court.FoodData
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.MenuViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    foodCourtId:String,
    shopId: String,
    menuViewModel: MenuViewModel
) {
    val scope = rememberCoroutineScope()

    val foodDataState = menuViewModel.foodDataState.collectAsStateWithLifecycle().value

    val selectedItems = menuViewModel.selectedItems.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit){
        scope.launch {
            menuViewModel.getFoodData(shopId)
        }
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
        totalAmountOfSelectedItems = items.sumOf { Integer.parseInt(it.first.price) * it.second }
        if(selectedItems.isEmpty()){
            scope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }else if (selectedItems.size >0){
            scaffoldState.bottomSheetState.expand()
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
                                modifier = Modifier.weight(0.3f),
                                color = colorResource(Constants.TEXT_COLOR)
                            )
                            Text(
                                text = "Quantity",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f),
                                color = colorResource(Constants.TEXT_COLOR)
                            )
                            Text(
                                text = "Price",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f),
                                color = colorResource(Constants.TEXT_COLOR)
                            )
                            Text(
                                text = "Total",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.2f),
                                color = colorResource(Constants.TEXT_COLOR)
                            )
                        }
                    }
                    items(selectedItems.toList()) { item ->
                        BillItemView(foodData = item, selectedItems = selectedItems)
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(Constants.PAYMENT_GREEN_COLOR))
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${selectedItems.size} | items ${Constants.getCurrency(totalAmountOfSelectedItems)}",
                                fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                                color = colorResource(Constants.TEXT_COLOR)
                            )
                            Row(
                                modifier = Modifier.clickable {
                                  navHostController.navigate(Screens.OrderPlaceScreen.route+"${shopId}/${foodCourtId}")
                                },
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Pay",
                                    fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                                    color = colorResource(Constants.TEXT_COLOR)
                                )
                                Icon(
                                    imageVector = Icons.Default.Paid,
                                    contentDescription = "",
                                    tint = colorResource(Constants.TEXT_COLOR)
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
                                    color = colorResource(Constants.BLUE_COLOR),
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
                                .border(1.dp, colorResource(Constants.TEXT_COLOR), RoundedCornerShape(18.dp)),
                            value = searchQuery,
                            textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
                            placeholder = {
                                Text(
                                    "Search Your Favourite Item...",
                                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                                    color = colorResource(Constants.TEXT_COLOR)
                                )
                            },
                            onValueChange = { searchQuery = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(Constants.BACKGROUND_COLOR),
                                unfocusedContainerColor = colorResource(Constants.BACKGROUND_COLOR),
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

                    if(foodDataState.isLoading){
                        item {
                            CircularProgressIndicator()
                        }
                    }else {
                        if(foodDataState.foodData != null){
                            items(foodDataState.foodData.data) { foodData ->

                                ProductItem(
                                    menuViewModel = menuViewModel,
                                    foodData = foodData
                                )

                            }
                        }else{
                            item {
                                Text(foodDataState.error,color = colorResource(Constants.TEXT_COLOR))
                            }
                        }
                    }

                }

            }

        }

    }

}


@Composable
private fun ProductItem(
    menuViewModel: MenuViewModel,
    foodData: FoodData
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp,
        )
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(Constants.BACKGROUND_COLOR))
                .border(1.dp, color = colorResource(Constants.TEXT_COLOR), shape = RoundedCornerShape(12.dp))
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
                    AsyncImage(
                        model = foodData.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
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
                            foodData.name,
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Text(
                            foodData.description,
                            fontSize = 10.sp,
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = Constants.getCurrency(foodData.price.toInt()),
                            color = colorResource(Constants.TEXT_COLOR),
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                        )
                        Icon(
                            imageVector = Icons.Default.RemoveCircle,
                            contentDescription = "remove_item",
                            modifier = Modifier.clickable {
                                menuViewModel.removeItem(foodData)
                            },
                            tint = colorResource(Constants.TEXT_COLOR)
                        )
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "add_item",
                            modifier = Modifier.clickable {

                                menuViewModel.addItem(foodData)

                            },
                            tint = colorResource(Constants.TEXT_COLOR)
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
                    Text("${foodData.preparationTime}",color = colorResource(Constants.TEXT_COLOR))
                }


            }

            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(colorResource(Constants.BLUE_COLOR))
                    .padding(horizontal = 24.dp, vertical = 2.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        menuViewModel.addItem(foodData = foodData)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Add",
                    color = colorResource(Constants.TEXT_COLOR),
                    fontFamily = Constants.POOPINS_FONT_REGULAR
                )
            }

        }

    }

}


@Composable
fun BillItemView(
    foodData: Pair<FoodData, Int>,
    modifier: Modifier = Modifier,
    selectedItems: Map<FoodData, Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = foodData.first.name,
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(0.3f),
            color = colorResource(Constants.TEXT_COLOR)
        )
        Text(
            text = "${foodData.second}",
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f),
            color = colorResource(Constants.TEXT_COLOR)
        )
        Text(
            text = Constants.getCurrency(foodData.first.price.toInt()),
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f),
            color = colorResource(Constants.TEXT_COLOR)
        )
        val total_price = Integer.parseInt(foodData.first.price) * foodData.second
        Text(
            text = Constants.getCurrency(total_price),
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.2f),
            color = colorResource(Constants.TEXT_COLOR)
        )
    }
}


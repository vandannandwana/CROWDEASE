package com.minor.crowdease.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.data.dto.food_court.FoodCourtData
import com.minor.crowdease.data.dto.food_court.FoodCourtDto
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.FoodCourtViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navHostController: NavHostController) {
    val foodCourtViewModel = hiltViewModel<FoodCourtViewModel>()
    val foodCourtState by foodCourtViewModel.foodCourtState.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Debounce search query to avoid excessive filtering
    val debouncedSearchQuery by remember(searchQuery) {
        derivedStateOf { searchQuery }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Food Courts",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        fontSize = 32.sp,
                        color = colorResource(Constants.TEXT_COLOR)
                    )
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Information about food courts",
                            tint = Color.Gray
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
                ) {}
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .border(1.dp, colorResource(Constants.TEXT_COLOR), RoundedCornerShape(18.dp)),
                    value = searchQuery,
                    textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
                    placeholder = {
                        Text(
                            text = "Search Food Courts ...",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                    },
                    onValueChange = { searchQuery = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(Constants.BACKGROUND_COLOR),
                        unfocusedContainerColor = colorResource(Constants.BACKGROUND_COLOR),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search food courts",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(start = 2.dp)
                        )
                    }
                )
            }

            when {
                foodCourtState.isLoading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                foodCourtState.error != null -> {
                    item {
                        Text(
                            text = foodCourtState.error ?: "Unknown error",
                            color = colorResource(Constants.TEXT_COLOR),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                foodCourtState.foodCourts != null -> {
                    val filteredFoodCourts = if (debouncedSearchQuery.isBlank()) {
                        foodCourtState.foodCourts!!.data
                    } else {
                        foodCourtState.foodCourts!!.data.filter {
                            it.name.lowercase().contains(debouncedSearchQuery.lowercase())
                        }
                    }

                    items(filteredFoodCourts) { foodCourt ->
                        FoodCourtItem(
                            foodCourt = foodCourt,
                            navHostController = navHostController,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun FoodCourtItem(
    foodCourt: FoodCourtData,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val foodCourtViewModel = hiltViewModel<FoodCourtViewModel>()
    val pendingOrdersState by foodCourtViewModel.getPendingOrdersState(foodCourt.id)
        .collectAsStateWithLifecycle()

    val rushStatus = when {
        pendingOrdersState.pendingOrders < 6 -> "Low Rush" to Color.Green
        pendingOrdersState.pendingOrders <= 15 -> "Mid Rush" to Color.Yellow
        else -> "High Rush" to Color.Red
    }

    val animatedColor by animateColorAsState(
        targetValue = rushStatus.second,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "rushColor"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                navHostController.navigate(Screens.ShopScreen.route + "${foodCourt.id}")
            },
        border = BorderStroke(1.dp, colorResource(Constants.TEXT_COLOR)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(Constants.BACKGROUND_COLOR)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Column {
            //Image and rush
            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {

                AsyncImage(
                    model = foodCourt.image,
                    contentDescription = "food_court_image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(animatedColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rushStatus.first,
                        color = Color.White,
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    )
                }

            }
            Spacer(modifier = Modifier.height(12.dp))
            //Details Section

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = foodCourt.name,
                    fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                    fontSize = 24.sp,
                    color = colorResource(Constants.TEXT_COLOR)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "star_ic",
                        tint = Color.Yellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "4.5",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        color = colorResource(Constants.TEXT_COLOR)
                    )
                }

            }

            Text(
                text = foodCourt.location,
                fontFamily = Constants.POOPINS_FONT_REGULAR,
                fontSize = 16.sp,
                color = colorResource(Constants.TEXT_COLOR),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "clock_icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "30 min",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        color = colorResource(Constants.TEXT_COLOR)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shop,
                        contentDescription = "clock_icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "4 Outlets",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        color = colorResource(Constants.TEXT_COLOR)
                    )
                }


            }


        }
        }
}
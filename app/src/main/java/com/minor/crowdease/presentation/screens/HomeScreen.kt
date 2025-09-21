package com.minor.crowdease.presentation.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.data.dto.food_court.FoodCourtData
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.FoodCourtViewModel
import com.minor.crowdease.presentation.viewmodels.HomeViewModel
import com.minor.crowdease.utlis.Constants

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val foodCourtViewModel = hiltViewModel<FoodCourtViewModel>()
    val foodCourtsState by foodCourtViewModel.foodCourtState.collectAsStateWithLifecycle()


    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Parul University",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            color = colorResource(Constants.ORANGE_COLOR),
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Food Court",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 12.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color.LightGray)
                            .clickable{
                                navHostController.navigate(Screens.OrderHistoryScreen.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "orders_history"
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

                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Column {
                        Text(
                            text = "Hey There!",
                            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                            fontSize = 24.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Text(
                            text = "What would you like to eat today?",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 16.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .border(
                            color = Color.LightGray,
                            width = 1.dp,
                            shape = RoundedCornerShape(7.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn {
                        item {
                            Text(
                                text = "Current Rush Status",
                                fontFamily = Constants.POOPINS_FONT_REGULAR,
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            )
                        }

                        foodCourtsState.foodCourts?.data?.let { foodCourts ->
                            items(foodCourts) { foodCourt ->
                                PendingOrderView(
                                    foodCourt = foodCourt,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Top Food Courts",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TopFoodCourts()
                }
            }

        }
    }
}


@Composable
fun PendingOrderView(
    foodCourt: FoodCourtData,
    modifier: Modifier = Modifier
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val pendingOrdersState by homeViewModel.getPendingOrdersState(foodCourt.id)
        .collectAsStateWithLifecycle()

    LaunchedEffect(pendingOrdersState) {
        Log.d("VANDANPENDING", "PendingOrderView of ${foodCourt.name}: $pendingOrdersState")
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (pendingOrdersState * 4) / 100f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    val animatedColor by animateColorAsState(
        targetValue = when {
            pendingOrdersState < 6 -> Color.Green.copy(alpha = 0.4f)
            pendingOrdersState <= 15 -> Color.Yellow.copy(alpha = 0.4f)
            else -> Color.Red.copy(alpha = 0.4f)
        },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "colorAnimation"
    )

    Column(modifier = modifier) {
        Text(
            text = foodCourt.name,
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            color = colorResource(Constants.ORANGE_COLOR),
            modifier = Modifier.padding(1.dp)
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(7.dp)),
            color = animatedColor,
            trackColor = Color.LightGray.copy(alpha = 0.4f),
        )
    }
}

val topFoodCourts = listOf(
    "Food Court 1",
    "Food Court 2",
    "Food Court 3",
    "Food Court 4",
    "Food Court 5",
    "Food Court 6",
)

fun randomColor(): Color{
    return when((0..2).random()){
        0 -> Color.Red
        1 -> Color.Green
        else -> Color.Yellow
    }
}

@Composable
fun TopFoodCourts(modifier: Modifier = Modifier) {

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {

        val randomNum = (0..2).random()

        items(topFoodCourts.size) { idx ->
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(R.drawable.puff_image),
                    contentDescription = "top_food_court_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = randomColor(), shape = CircleShape)
                    ,
                )
                Text(
                    text = "Food Court" + (idx + 1),
                    fontSize = 12.sp
                )
            }
        }

    }



}



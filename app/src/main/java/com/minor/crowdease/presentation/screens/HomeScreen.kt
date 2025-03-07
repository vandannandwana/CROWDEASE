package com.minor.crowdease.presentation.screens
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.data.dto.food_court.FoodCourtData
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
                            color = colorResource(Constants.BLUE_COLOR),
                            fontSize = 32.sp
                        )
                        Text(
                            text = "Food Court",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 18.sp,
                            color = colorResource(Constants.TEXT_COLOR)
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
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "notification_icon"
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

                Box(modifier = Modifier.padding(12.dp)) {
                    Column {
                        Text(
                            text = "Hey There!",
                            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                            fontSize = 34.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Text(
                            text = "What would you like to eat today?",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 18.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            // Move food court items here to avoid nested LazyColumn
            foodCourtsState.foodCourts?.data?.let { foodCourts ->
                items(foodCourts) { foodCourt ->
                    PendingOrderView(
                        foodCourt = foodCourt,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
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
    val pendingOrdersState by homeViewModel.getPendingOrdersState(foodCourt.id).collectAsStateWithLifecycle()

    LaunchedEffect(pendingOrdersState){
        Log.d("VANDANPENDING", "PendingOrderView of ${foodCourt.name}: $pendingOrdersState")
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (pendingOrdersState * 4) / 100f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    val animatedColor by animateColorAsState(
        targetValue = when {
            pendingOrdersState < 6 -> Color.Green
            pendingOrdersState <= 15 -> Color.Yellow
            else -> Color.Red
        },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "colorAnimation"
    )

    Column(modifier = modifier) {
        Text(
            text = foodCourt.name,
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            color = colorResource(Constants.GREEN_COLOR)
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(7.dp)),
            color = animatedColor,
            trackColor = Color(0xFF22C45E).copy(alpha = 0.2f),
        )
    }
}

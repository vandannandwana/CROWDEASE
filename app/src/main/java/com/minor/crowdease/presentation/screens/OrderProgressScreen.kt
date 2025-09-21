package com.minor.crowdease.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.data.dto.order_history_info.Item
import com.minor.crowdease.utlis.Constants
import com.minor.crowdease.presentation.viewmodels.OrderHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class OrderStep(val title: String) {
    ORDER_PENDING("PENDING"),
    ORDER_CONFIRMED("CONFIRMED"),
    ORDER_PREPARING("PREPARING"),
    ORDER_COMPLETED("COMPLETED"),
    ORDER_CANCELLED("CANCELLED"),
}

var orderSteps = mapOf(
    "PENDING" to OrderStep.ORDER_PENDING,
    "CONFIRMED" to OrderStep.ORDER_CONFIRMED,
    "PREPARING" to OrderStep.ORDER_PREPARING,
    "COMPLETED" to OrderStep.ORDER_COMPLETED,
    "CANCELLED" to OrderStep.ORDER_CANCELLED
)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OrderProgressScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    orderID: String
) {
    val scope = rememberCoroutineScope()
    val orderHistoryViewModel = hiltViewModel<OrderHistoryViewModel>()
    val orderHistoryInfoState =
        orderHistoryViewModel.orderHistoryInfoState.collectAsStateWithLifecycle().value

    //calling order History function
    LaunchedEffect(orderHistoryViewModel.orderHistoryInfoState) {
        scope.launch(Dispatchers.IO) {
            orderHistoryViewModel.getOrderHistoryInfo(orderID)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip)
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Order Progress",
                textAlign = TextAlign.Center,
                fontFamily = Constants.POOPINS_FONT_BOLD,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.05f)
                    .padding(top = 12.dp),
                color = Color.White
            )

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .weight(0.8f)
                    .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                    .background(color = Color.White)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Cart",
                    textAlign = TextAlign.Center,
                    fontFamily = Constants.POOPINS_FONT_BOLD,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    color = Color.Black
                )

                if (orderHistoryInfoState.isLoading) {
                    CircularProgressIndicator()
                } else if (orderHistoryInfoState.error != null) {
                    Text(text = orderHistoryInfoState.error)
                }

                if (orderHistoryInfoState.orderHistoryInfo != null) {
                    //Food List Pager
                    FoodCartPagerView(foodCartItems = orderHistoryInfoState.orderHistoryInfo.data.items)

                    //Bill Section
                    BillSection(foodCartItems = orderHistoryInfoState.orderHistoryInfo.data.items, total = orderHistoryInfoState.orderHistoryInfo.data.totalAmount)

                    Text(
                        text = "Current Status",
                        textAlign = TextAlign.Center,
                        fontFamily = Constants.POOPINS_FONT_BOLD,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        color = Color.Black
                    )

                    val currentStep = orderSteps[orderHistoryInfoState.orderHistoryInfo.data.status]!!

                    //Current Food Status
                    FoodOrderProgressBar(currentStep = currentStep)

                    //Rating Section
                    RateUs(currentStep = currentStep)
                }
            }
        }
    }

}

@Composable
fun BillSection(
    foodCartItems: List<Item>,
    total : String
) {
    val rowHeight = (35.dp).times(foodCartItems.size)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Bill Summary",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(foodCartItems) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = item.menuItem.name,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "x${item.quantity}",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(0.3f)
                    )
                    Text(
                        text = "₹${item.menuItem.price}",
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.3f)
                    )
                }
            }
        }

        Divider()

        // Total row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "₹$total",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun FoodOrderProgressBar(
    currentStep: OrderStep,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.Gray,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val steps = OrderStep.entries.toTypedArray() // Get all defined order steps

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep == OrderStep.ORDER_CANCELLED) {
            Text("Sorry your Order is Canceled Due to some reason your money will be refunded in few minutes")
        } else {
            for (index in 0..steps.size-2) {
                val isActive = index <= steps.indexOf(currentStep)
                val stepColor by animateColorAsState(
                    targetValue = if (isActive) activeColor else inactiveColor,
                    animationSpec = tween(durationMillis = 500)
                )
                // Step indicator (circle and text)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f) // Distribute weight evenly
                ) {
                    // Circle indicator
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(stepColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Step title
                    Text(
                        text = steps[index].title,
                        color = if (isActive) activeColor else textColor,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1 // Ensure text doesn't wrap excessively
                    )
                }

                // Connector line (if not the last step)
                if (index < steps.size - 2) {
                    val lineColor by animateColorAsState(
                        targetValue = if (isActive) activeColor else inactiveColor,
                        animationSpec = tween(durationMillis = 500)
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 4.dp),
                        thickness = DividerDefaults.Thickness, color = lineColor
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodCartPagerView(
    foodCartItems: List<Item>
) {
    val pagerState = rememberPagerState(pageCount = { foodCartItems.size })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            FoodCartItemView(foodCartItem = foodCartItems[page])
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomPagerIndicator(
            totalPages = foodCartItems.size,
            currentPage = pagerState.currentPage
        )
    }
}

@Composable
fun CustomPagerIndicator(
    totalPages: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            val color by animateColorAsState(
                targetValue = if (index == currentPage)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                label = ""
            )

            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 12.dp else 8.dp) // active dot is bigger
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}


@Composable
private fun FoodCartItemView(
    foodCartItem: Item
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
                .border(
                    1.dp,
                    color = colorResource(Constants.TEXT_COLOR),
                    shape = RoundedCornerShape(12.dp)
                )
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
                        model = foodCartItem.menuItem.image,
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
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            foodCartItem.menuItem.name,
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                            fontSize = 16.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "₹ "+foodCartItem.menuItem.price,
                            fontSize = 14.sp,
                            color = colorResource(Constants.TEXT_COLOR),
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RateUs(modifier: Modifier = Modifier, currentStep: OrderStep) {
    var selectedRating by remember { mutableIntStateOf(0) }
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rate your experience:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach { starIndex ->
                Icon(
                    imageVector = if (starIndex <= selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$starIndex Star",
                    tint = if (starIndex <= selectedRating) colorResource(Constants.PAYMENT_GREEN_COLOR) else Color.Gray,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = false,
                                radius = 28.dp,
                                color = Color.Yellow
                            )
                        ) {
                            selectedRating = starIndex
                        }
                )
            }
        }

        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("Your comments") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5,
            minLines = 3,
        )

        Button(
            onClick = {
                println("Submitted Rating: $selectedRating stars")
                println("Submitted Comment: $commentText")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Review")
        }
    }
}
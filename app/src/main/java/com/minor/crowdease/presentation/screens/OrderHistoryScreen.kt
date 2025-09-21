package com.minor.crowdease.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.data.dto.order_history.Data
import com.minor.crowdease.data.dto.order_history_info.OrderHistoryInfoState
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.OrderHistoryViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderHistoryScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val orderHistoryViewModel = hiltViewModel<OrderHistoryViewModel>()
    val orderHistoryState = orderHistoryViewModel.orderHistoryState.collectAsStateWithLifecycle().value

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
                text = "My Orders",
                textAlign = TextAlign.Center,
                fontFamily = Constants.POOPINS_FONT_BOLD,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.05f)
                    .padding(top = 12.dp),
                color = Color.White
            )

            if (orderHistoryState.isLoading) {
                CircularProgressIndicator()
            }else if (orderHistoryState.error != null) {
                Text(text = orderHistoryState.error)
            }else if (orderHistoryState.orderHistory != null) {
                //OrderItems List
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .weight(0.8f)
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(color = Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Image(
                            painter = painterResource(R.drawable.order_history_cartoon),
                            contentDescription = "order_history_cartoon",
                            modifier = Modifier
                                .size(96.dp)
                        )
                    }
                    items(orderHistoryState.orderHistory.data) {orderHistoryItem->
                        OrderItemView(
                            orderItem = orderHistoryItem,
                            navHostController = navHostController,
                            orderHistoryViewModel = orderHistoryViewModel
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun OrderItemView(
    modifier: Modifier = Modifier,
    orderItem: Data,
    navHostController: NavHostController,
    orderHistoryViewModel: OrderHistoryViewModel
) {
    val formattedDate = try {
        Instant.parse(orderItem.createdAt)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        orderItem.createdAt
    }

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                CoroutineScope(Dispatchers.IO).launch {
                    orderHistoryViewModel._orderHistoryInfoState.value = OrderHistoryInfoState(orderHistoryInfo = null)
                    orderHistoryViewModel.getOrderHistoryInfo(orderItem.id)
                }
                navHostController.navigate(Screens.OrderProgressScreen.route+"/${orderItem.id}")
            },
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.2f),

            ),
        border = CardDefaults.outlinedCardBorder(enabled = true)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.puff_image),
                contentDescription = "order_item_image",
                modifier = Modifier
                    .weight(0.3f)
                    .size(120.dp)
                    .clip(RoundedCornerShape(17.dp)),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(0.7f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = orderItem.orderNumber,
                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Price: ₹${orderItem.totalAmount}",
                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Date: $formattedDate",
                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

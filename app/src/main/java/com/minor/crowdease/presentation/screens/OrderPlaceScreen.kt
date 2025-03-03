package com.minor.crowdease.presentation.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.PaymentActivity
import com.minor.crowdease.R
import com.minor.crowdease.data.dto.order_place.PlaceOrderItemDto
import com.minor.crowdease.data.dto.order_place.main.PlaceOrderDto
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.MenuViewModel
import com.minor.crowdease.presentation.viewmodels.OrdersViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch

@Composable
fun OrderPlaceScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    menuViewModel: MenuViewModel,
    foodCourtId:String,
    shopId:String
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val ordersViewModel = hiltViewModel<OrdersViewModel>()

    var totalAmountOfSelectedItems by remember {
        mutableIntStateOf(0)
    }
    val selectedItems = menuViewModel.selectedItems.collectAsStateWithLifecycle().value

    val placedOrders by remember {
        mutableStateOf(mutableListOf<PlaceOrderItemDto>())
    }

    LaunchedEffect(selectedItems.toList()) {
        val items = selectedItems.toList()
        totalAmountOfSelectedItems = items.sumOf { Integer.parseInt(it.first.price) * it.second }
        for (item in items) {
            val i  = item.first
            placedOrders.add(PlaceOrderItemDto(
                price = i.price.toDouble(),
                menuItemId = i.id,
                quantity = item.second
            ))
        }
    }

    var paymentStatus by remember {
        mutableStateOf("")
    }

    val paymentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result->

        if(result.resultCode == Activity.RESULT_OK){

            val paymentId = result.data?.getStringExtra("paymentId")
            Log.d("VANDANPAY", "$paymentId ")
            if(paymentId != null){

                val placedOrder = PlaceOrderDto(
                    shopId = shopId,
                    foodCourtId = foodCourtId,
                    paymentMethod = "UPI",
                    items =placedOrders,
                    totalAmount = totalAmountOfSelectedItems.toDouble()
                )
                scope.launch{
                    ordersViewModel.placeOrder(placeOrderDto = placedOrder)
                }
                navHostController.navigate(Screens.SuccessScreen.route+"{$paymentId}")
            }
        }else{
            paymentStatus = "Failed"
        }


    }


    val paymentMethods = listOf(
        PaymentMethod(
            name = "GPay",
            icon = R.drawable.gpay,
            onClick = {
                val intent = Intent(context,PaymentActivity::class.java)
                intent.putExtra("totalAmount", totalAmountOfSelectedItems)
                paymentLauncher.launch(intent)
            }
        ),
        PaymentMethod(
            name = "Paytm",
            icon = R.drawable.paytm,
            onClick = {

                val intent = Intent(context, PaymentActivity::class.java)
                intent.putExtra("totalAmount", totalAmountOfSelectedItems)
                context.startActivity(intent)
            }
        ),
        PaymentMethod(
            name = "PhonePe",
            icon = R.drawable.phonepe,
            onClick = {
                val intent = Intent(context, PaymentActivity::class.java)
                intent.putExtra("totalAmount", totalAmountOfSelectedItems)
                context.startActivity(intent)
            }
        )
    )


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .clickable {
                                navHostController.popBackStack()
                            },
                        shape = RoundedCornerShape(7.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(Constants.GREEN_COLOR)
                        )
                    ) {
                        Text(
                            text = "Close",
                            fontSize = 18.sp,
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Bill Total: ${Constants.getCurrency(totalAmountOfSelectedItems)}",
                        fontSize = 18.sp,
                        fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                        color = colorResource(Constants.TEXT_COLOR)
                    )

                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    item {
                        Text(
                            text = "Payment Methods",
                            fontSize = 22.sp,
                            color = colorResource(Constants.GREY),
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            modifier = Modifier.padding(24.dp)
                        )
                    }

                    items(paymentMethods){method->

                        PaymentMethodItem(paymentMethod = method)

                    }

                }

            }

        }

    }

}

data class PaymentMethod(
    val name: String,
    val icon: Int,
    val onClick: () -> Unit
)

@Composable
fun PaymentMethodItem(paymentMethod: PaymentMethod) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                paymentMethod.onClick()
            }
            .padding(horizontal = 24.dp, vertical = 7.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(colorResource(Constants.BACKGROUND_COLOR))
            .border(1.dp, colorResource(Constants.TEXT_COLOR), RoundedCornerShape(7.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(paymentMethod.icon),
            contentDescription = "profile_icon",
            modifier = Modifier
                .weight(0.2f)
                .height(42.dp),
            tint = colorResource(Constants.TEXT_COLOR)
        )

        Text(
            text = paymentMethod.name,
            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
            modifier = Modifier.weight(0.6f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "profile_icon",
            modifier = Modifier.weight(0.2f),
        )
    }
}







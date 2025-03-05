package com.minor.crowdease.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.data.dto.shop.Shop
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.ShopViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    foodCourtId: String
) {
    val shopViewModel = hiltViewModel<ShopViewModel>()

    val shopState = shopViewModel.shopState.collectAsStateWithLifecycle().value

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        scope.launch {
            shopViewModel.getShops(foodCourtId = foodCourtId)
        }

    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){

            if(shopState.isLoading){
                item { CircularProgressIndicator() }
            }
            else{

                if(shopState.error != null){
                    item {
                        Text(text = shopState.error)
                    }
                }
                else if (shopState.shopCourts != null){

                    items(shopState.shopCourts.shops){shop->
                        ShopItem(
                            shopData = shop,
                            navHostController = navHostController,
                            scope = scope,
                            shopViewModel = shopViewModel,
                            foodCourtId = foodCourtId
                        )
                    }

                }

            }


        }

    }

}


@Composable
fun ShopItem(
    shopData: Shop,
    shopViewModel: ShopViewModel,
    scope: CoroutineScope,
    navHostController: NavHostController,
    foodCourtId: String
) {


    var pendingOrders by rememberSaveable {
        mutableIntStateOf(0)
    }

    var text by remember {
        mutableStateOf("Low Rush")
    }

    val animatePendingOrderColor by animateColorAsState(
        targetValue = if (pendingOrders<6) Color.Green else if (pendingOrders<=15) Color.Yellow else Color.Red,
        animationSpec = tween(durationMillis = 1200)
    )

    LaunchedEffect(Unit){
        scope.launch {
            pendingOrders = shopViewModel.getPendingOrders(shopId = shopData.id)
            text = if (pendingOrders<6) "Low Rush" else if (pendingOrders<=15) "Mid Rush" else "High Rush"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                navHostController.navigate(Screens.MenuScreen.route+"${shopData.id}/${foodCourtId}")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(Constants.BACKGROUND_COLOR)
        ),
        border = BorderStroke(1.dp, colorResource(Constants.TEXT_COLOR)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Column{
            //Image and rush
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = shopData.images,
                    contentDescription = "food_court_image",
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(animatePendingOrderColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text=text,
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
                    text = shopData.name,
                    fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                    fontSize = 28.sp,
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
                text = shopData.contactPhone,
                fontFamily = Constants.POOPINS_FONT_REGULAR,
                fontSize = 18.sp,
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
                    Text("30 min", fontFamily = Constants.POOPINS_FONT_REGULAR,color = colorResource(Constants.TEXT_COLOR))
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
                    Text("4 Outlets", fontFamily = Constants.POOPINS_FONT_REGULAR,color = colorResource(Constants.TEXT_COLOR))
                }


            }


        }

    }

}
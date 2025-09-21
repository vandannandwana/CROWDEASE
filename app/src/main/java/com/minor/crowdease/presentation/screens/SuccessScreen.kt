package com.minor.crowdease.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.minor.crowdease.R
import com.minor.crowdease.navigations.Screens
import kotlinx.coroutines.delay

@Composable
fun SuccessScreen(
    navHostController: NavHostController,
    isSuccess: Boolean
) {

    LaunchedEffect(Unit) {
        delay(5000)
        navHostController.navigate(Screens.MainScreen.route){
            popUpTo(Screens.MainScreen.route){
                inclusive = true
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            ,
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSuccess) Color.Green.copy(alpha = 0.4f) else Color.Red.copy(alpha = 0.4f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .padding(ip)
            ) {
                val successComposition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(resId = R.raw.order_place_success)
                )
                val failedComposition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(resId = R.raw.order_place_failed)
                )
                if (isSuccess) {
                    LottieAnimation(successComposition)
                } else {
                    LottieAnimation(failedComposition)
                }
            }
            Text(
                text = if (isSuccess) "Order Placed Successfully" else "Order Failed",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }
}

@Composable
fun LottieAnimationView(modifier: Modifier = Modifier, composition: LottieComposition) {
    val preLoaderProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        speed = 1.0f
    )
    LottieAnimation(
        composition = composition,
        progress = preLoaderProgress,
        modifier = modifier.size(220.dp)
    )
}
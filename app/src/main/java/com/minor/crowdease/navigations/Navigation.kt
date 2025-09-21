package com.minor.crowdease.navigations

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.minor.crowdease.presentation.screens.HomeScreen
import com.minor.crowdease.presentation.screens.LoginScreen
import com.minor.crowdease.presentation.screens.MainScreen
import com.minor.crowdease.presentation.screens.MenuScreen
import com.minor.crowdease.presentation.screens.MessageScreen
import com.minor.crowdease.presentation.screens.OrderHistoryScreen
import com.minor.crowdease.presentation.screens.OrderPlaceScreen
import com.minor.crowdease.presentation.screens.OrderProgressScreen
import com.minor.crowdease.presentation.screens.OtpScreen
import com.minor.crowdease.presentation.screens.ProfileEditScreen
import com.minor.crowdease.presentation.screens.ProfileScreen
import com.minor.crowdease.presentation.screens.SearchScreen
import com.minor.crowdease.presentation.screens.ShopScreen
import com.minor.crowdease.presentation.screens.SuccessScreen
import com.minor.crowdease.presentation.viewmodels.MenuViewModel
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF


@Composable
fun Navigation(navHostController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(TOKENPREF, Context.MODE_PRIVATE)
    val token = sharedPreferences.getString(TOKENPREF, null)


    val menuViewModel = hiltViewModel<MenuViewModel>()

    NavHost(
        navController = navHostController,
        startDestination = /*Screens.SuccessScreen.route+"/false"*/ if (token == null) Screens.LoginScreen.route else Screens.MainScreen.route
    ) {

        composable(route = Screens.MainScreen.route) {
            MainScreen(navHostController = navHostController)
        }

        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navHostController = navHostController)
        }

        composable(route = Screens.SearchScreen.route) {
            SearchScreen(navHostController = navHostController)
        }

        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navHostController = navHostController)
        }
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navHostController = navHostController)
        }

        composable(Screens.ProfileEditScreen.route) {
            ProfileEditScreen(navHostController = navHostController)
        }

        composable(Screens.MenuScreen.route + "{shopId}/{foodCourtId}") {
            val shopId = it.arguments?.getString("shopId")
            val foodCourtId = it.arguments?.getString("foodCourtId")
            if (shopId != null && foodCourtId != null) {
                MenuScreen(
                    navHostController = navHostController,
                    menuViewModel = menuViewModel,
                    foodCourtId = foodCourtId,
                    shopId = shopId
                )
            }
        }

        composable(Screens.OtpScreen.route) {
            OtpScreen(navHostController = navHostController)
        }

        composable(Screens.ShopScreen.route + "{foodCourtId}") {
            val foodCourtId = it.arguments?.getString("foodCourtId")
            if (foodCourtId != null) {
                ShopScreen(navHostController = navHostController, foodCourtId = foodCourtId)
            }
        }

        composable(Screens.OrderPlaceScreen.route + "{shopId}/{foodCourtId}") {
            val shopId = it.arguments?.getString("shopId")
            val foodCourtId = it.arguments?.getString("foodCourtId")
            if (shopId != null && foodCourtId != null) {
                OrderPlaceScreen(
                    navHostController = navHostController,
                    menuViewModel = menuViewModel,
                    foodCourtId = foodCourtId,
                    shopId = shopId
                )
            }
        }

        composable(
            route = Screens.SuccessScreen.route + "/{isSuccess}",
            arguments = listOf(
                navArgument("isSuccess") {
                    type = NavType.BoolType
                }
            )
        ) {
            val isSuccess = it.arguments?.getBoolean("isSuccess") ?: false
            SuccessScreen(isSuccess = isSuccess, navHostController = navHostController)
        }

        composable(Screens.MessageScreen.route) {
            MessageScreen()
        }

        composable(route = Screens.OrderHistoryScreen.route) {
            OrderHistoryScreen(navHostController = navHostController)
        }

        composable(
            route = Screens.OrderProgressScreen.route + "/{orderID}",
            arguments = listOf(
                navArgument("orderID") {
                    type = NavType.StringType
                }
            )
        ) {
            val orderID = it.arguments?.getString("orderID")
            if (orderID != null) {
                OrderProgressScreen(navHostController = navHostController, orderID = orderID)
            }
        }
    }
}
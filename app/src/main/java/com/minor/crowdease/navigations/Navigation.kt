package com.minor.crowdease.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.minor.crowdease.presentation.screens.HomeScreen
import com.minor.crowdease.presentation.screens.LoginScreen
import com.minor.crowdease.presentation.screens.MainScreen
import com.minor.crowdease.presentation.screens.MenuScreen
import com.minor.crowdease.presentation.screens.OtpScreen
import com.minor.crowdease.presentation.screens.ProfileScreen
import com.minor.crowdease.presentation.screens.SearchScreen
import com.minor.crowdease.presentation.screens.ShopScreen


@Composable
fun Navigation(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Screens.LoginScreen.route) {

        composable(route = Screens.MainScreen.route){
            MainScreen(navHostController = navHostController)
        }

        composable(route = Screens.HomeScreen.route){
            HomeScreen(navHostController = navHostController)
        }

        composable(route = Screens.SearchScreen.route){
            SearchScreen(navHostController = navHostController)
        }

        composable(route = Screens.ProfileScreen.route){
            ProfileScreen(navHostController = navHostController)
        }
        composable(route = Screens.LoginScreen.route){
            LoginScreen(navHostController = navHostController)
        }

        composable(Screens.MenuScreen.route){
            MenuScreen(navHostController = navHostController)
        }

        composable(Screens.OtpScreen.route){
            OtpScreen(navHostController = navHostController)
        }

        composable(Screens.ShopScreen.route+"{foodCourtId}"){
            val foodCourtId = it.arguments?.getString("foodCourtId")
            if (foodCourtId != null) {
                ShopScreen(navHostController = navHostController, foodCourtId = foodCourtId)
            }
        }



    }



}
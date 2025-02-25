package com.minor.crowdease.navigations

sealed class Screens(val route:String){

    data object MainScreen:Screens(route = "main_screen")
    data object HomeScreen:Screens(route = "home_screen")
    data object SearchScreen:Screens(route = "search_screen")
    data object ProfileScreen:Screens(route = "profile_screen")
    data object LoginScreen:Screens(route = "login_screen")
    data object MenuScreen:Screens(route = "menu_screen")
    data object OtpScreen:Screens(route = "otp_screen")
    data object ShopScreen:Screens(route = "shop_screen")

}
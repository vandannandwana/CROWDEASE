package com.minor.crowdease.data.dto.food_court

data class LoginState(
    val loginResponse: LoginResponse? = null,
    val isLoading:Boolean  = false,
    val error:String? = null
)
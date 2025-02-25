package com.minor.crowdease.data.dto.food_court

data class RegisterState(
val loginResponse: RegisterResponse? = null,
val isLoading:Boolean  = false,
val error:String? = null
)

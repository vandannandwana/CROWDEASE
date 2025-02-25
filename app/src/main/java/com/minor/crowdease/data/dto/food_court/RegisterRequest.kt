package com.minor.crowdease.data.dto.food_court

data class RegisterRequest(
    val name:String,
    val email:String,
    val password: String,
    val phoneNumber :String
)
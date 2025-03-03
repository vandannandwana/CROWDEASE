package com.minor.crowdease.data.dto.login

data class RegisterRequest(
    val name:String,
    val email:String,
    val password: String,
    val phoneNumber :String
)
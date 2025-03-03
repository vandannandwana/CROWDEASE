package com.minor.crowdease.data.dto.login

data class RegisterState(
    val loginResponse: RegisterResponse? = null,
    val isLoading:Boolean  = false,
    val error:String? = null
)

package com.minor.crowdease.data.dto.login

data class LoginState(
    val loginResponse: LoginResponse? = null,
    val isLoading:Boolean  = false,
    val error:String? = null
)
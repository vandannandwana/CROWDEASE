package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.food_court.LoginRequest
import com.minor.crowdease.data.dto.food_court.LoginResponse
import com.minor.crowdease.data.dto.food_court.RegisterRequest
import com.minor.crowdease.data.dto.food_court.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/v1/student/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/v1/student/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

}
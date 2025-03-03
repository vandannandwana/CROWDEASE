package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.login.LoginRequest
import com.minor.crowdease.data.dto.login.LoginResponse
import com.minor.crowdease.data.dto.login.OtpRequest
import com.minor.crowdease.data.dto.login.OtpResponse
import com.minor.crowdease.data.dto.login.RegisterRequest
import com.minor.crowdease.data.dto.login.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @POST("/api/v1/student/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/v1/student/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("/api/v1/student/verfify")
    suspend fun verify(@Header ("Authorization") token:String, @Body optRequest: OtpRequest): OtpResponse

}
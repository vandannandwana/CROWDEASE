package com.minor.crowdease.data.dto.chat

import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {

    @POST("/send")
    suspend fun sendMessage(
        @Body message: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body  message: SendMessageDto
    )

}
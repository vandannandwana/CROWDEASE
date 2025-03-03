package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.order_place.main.PlaceOrderDto
import com.minor.crowdease.data.dto.order_place.main.PlaceOrderResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderService {

    @POST("/api/v1/student/placeorder")
    suspend fun placeOrder(@Header("Authorization") token:String, @Body placeOrderDto: PlaceOrderDto): PlaceOrderResponseDto

}
package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.order_history.OrderHistoryDto
import com.minor.crowdease.data.dto.order_history_info.OrderHistoryInfoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OrderHistoryService {
    @GET("/api/v1/student/getmyorders")
    suspend fun getOrderHistory(@Header("Authorization") token: String): OrderHistoryDto

    @GET("/api/v1/student/orderdetails/{orderID}")
    suspend fun getOrderHistoryInfo(@Header("Authorization") token: String, @Path("orderID") orderID: String): OrderHistoryInfoDto
}
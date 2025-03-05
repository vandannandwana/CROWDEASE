package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.food_court.FoodCourtDto
import com.minor.crowdease.data.dto.food_court.PendingOrderDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodCourtService {

    @GET("/api/v1/foodcourt")
    suspend fun getFoodCourts(): FoodCourtDto

    @GET("/api/v1/foodcourt/pendingorders/{foodCourtId}")
    suspend fun pendingOrderFoodCourt(@Path("foodCourtId") foodCourtId: String): PendingOrderDto


}
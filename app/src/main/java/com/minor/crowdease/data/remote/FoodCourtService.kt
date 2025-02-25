package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.food_court.FoodCourtDto
import retrofit2.http.GET

interface FoodCourtService {

    @GET("/api/v1/foodcourt")
    suspend fun getFoodCourts(): FoodCourtDto

}
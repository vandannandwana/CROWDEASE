package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.shop.ShopDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopService {

    @GET("/api/v1/foodcourt/getallshops/{foodCourtId}")
    suspend fun getShops(@Path("foodCourtId") foodCourtId: String): ShopDto

}
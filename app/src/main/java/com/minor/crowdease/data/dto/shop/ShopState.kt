package com.minor.crowdease.data.dto.shop

import com.minor.crowdease.data.dto.food_court.FoodCourtDto

data class ShopState(
    val shopCourts: ShopDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
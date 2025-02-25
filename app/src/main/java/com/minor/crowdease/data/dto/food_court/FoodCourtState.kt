package com.minor.crowdease.data.dto.food_court

data class FoodCourtState(
    val foodCourts: FoodCourtDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
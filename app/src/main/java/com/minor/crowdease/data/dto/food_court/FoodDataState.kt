package com.minor.crowdease.data.dto.food_court

data class FoodDataState(
    val foodData: FoodDto? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

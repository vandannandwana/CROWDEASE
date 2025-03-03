package com.minor.crowdease.data.dto.order_place

import com.minor.crowdease.data.dto.order_place.main.PlaceOrderResponseDto

data class PlacedOrderState(
    val isLoading:Boolean = false,
    val placeOrderResponseDto: PlaceOrderResponseDto? = null,
    val error:String? = null
)

package com.minor.crowdease.data.dto.order_place.main

import com.minor.crowdease.data.dto.order_place.PlaceOrderResponseOrderDto

data class PlaceOrderResponseDto(
    val message: String,
    val order: PlaceOrderResponseOrderDto
)
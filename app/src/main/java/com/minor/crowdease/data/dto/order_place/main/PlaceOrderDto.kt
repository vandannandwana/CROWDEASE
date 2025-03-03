package com.minor.crowdease.data.dto.order_place.main

import com.minor.crowdease.data.dto.order_place.PlaceOrderItemDto

data class PlaceOrderDto(
    val foodCourtId: String,
    val items: List<PlaceOrderItemDto>,
    val paymentMethod: String,
    val shopId: String,
    val totalAmount: Double
)
package com.minor.crowdease.data.dto.order_place

data class PlaceOrderItemDto(
    val menuItemId: String,
    val price: Double,
    val quantity: Int
)
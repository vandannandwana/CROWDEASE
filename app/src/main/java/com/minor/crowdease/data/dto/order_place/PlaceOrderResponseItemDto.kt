package com.minor.crowdease.data.dto.order_place

data class PlaceOrderResponseItemDto(
    val id: String,
    val menuItemId: String,
    val notes: Any,
    val orderId: String,
    val price: String,
    val quantity: Int
)
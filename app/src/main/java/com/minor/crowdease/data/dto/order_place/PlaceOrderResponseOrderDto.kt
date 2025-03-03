package com.minor.crowdease.data.dto.order_place

data class PlaceOrderResponseOrderDto(
    val createdAt: String,
    val foodCourtId: String,
    val id: String,
    val items: List<PlaceOrderResponseItemDto>,
    val orderNumber: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val preparationTime: Any,
    val shopId: String,
    val status: String,
    val studentId: String,
    val totalAmount: String,
    val updatedAt: String
)
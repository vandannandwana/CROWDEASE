package com.minor.crowdease.data.dto.order_history

data class Data(
    val createdAt: String,
    val id: String,
    val orderNumber: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val status: String,
    val totalAmount: String
)
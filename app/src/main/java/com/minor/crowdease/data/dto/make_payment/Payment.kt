package com.minor.crowdease.data.dto.make_payment

data class Payment(
    val id: String,
    val orderNumber: String,
    val paymentStatus: String,
    val shopId: String,
    val totalAmount: String
)
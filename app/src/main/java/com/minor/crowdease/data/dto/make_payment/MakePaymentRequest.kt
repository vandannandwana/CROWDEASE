package com.minor.crowdease.data.dto.make_payment

data class MakePaymentRequest(
    val foodCourtId: String,
    val orderId: String,
    val paymentId: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val shopId: String,
    val totalAmount: Double
)
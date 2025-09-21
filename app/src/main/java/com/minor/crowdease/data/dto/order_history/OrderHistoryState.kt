package com.minor.crowdease.data.dto.order_history

data class OrderHistoryState(
    val isLoading: Boolean = false,
    val orderHistory: OrderHistoryDto? = null,
    val error: String? = null
)
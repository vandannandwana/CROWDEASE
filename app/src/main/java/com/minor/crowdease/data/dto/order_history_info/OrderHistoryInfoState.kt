package com.minor.crowdease.data.dto.order_history_info

data class OrderHistoryInfoState(
    val isLoading: Boolean = false,
    val orderHistoryInfo: OrderHistoryInfoDto? = null,
    val error: String? = null
)
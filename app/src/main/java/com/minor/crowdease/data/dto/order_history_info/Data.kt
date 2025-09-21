package com.minor.crowdease.data.dto.order_history_info

data class Data(
    val id: String,
    val items: List<Item>,
    val status: String,
    val totalAmount: String
)
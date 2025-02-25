package com.minor.crowdease.data.dto.shop

data class Shop(
    val contactEmail: String,
    val contactPhone: String,
    val createdAt: String,
    val description: String,
    val foodCourtId: String,
    val gstNumber: String,
    val id: String,
    val images: String,
    val isActive: Boolean,
    val license: String,
    val name: String,
    val orders: List<Order>,
    val shopKeeperId: String,
    val updatedAt: String
)
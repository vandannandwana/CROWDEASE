package com.minor.crowdease.data.dto.user


data class UpdateState (
    val isUpdating:Boolean = false,
    val isUpdated:Boolean = false,
    val error: String? = null
)
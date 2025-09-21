package com.minor.crowdease.data.dto.make_payment

data class MakePaymentState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val data : MakePaymentResponse? = null
)
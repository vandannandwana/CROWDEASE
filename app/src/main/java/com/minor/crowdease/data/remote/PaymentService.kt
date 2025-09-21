package com.minor.crowdease.data.remote

import com.minor.crowdease.data.dto.make_payment.MakePaymentRequest
import com.minor.crowdease.data.dto.make_payment.MakePaymentResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentService {
    @POST("/api/v1/student/makepayment")
    suspend fun makePayment(@Header(value = "Authorization") token: String, @Body makePaymentRequest: MakePaymentRequest): MakePaymentResponse
}
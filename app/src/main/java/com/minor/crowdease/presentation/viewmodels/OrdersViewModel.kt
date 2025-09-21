package com.minor.crowdease.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.dto.make_payment.MakePaymentRequest
import com.minor.crowdease.data.dto.make_payment.MakePaymentState
import com.minor.crowdease.data.dto.order_place.main.PlaceOrderDto
import com.minor.crowdease.data.dto.order_place.PlacedOrderState
import com.minor.crowdease.data.remote.OrderService
import com.minor.crowdease.data.remote.PaymentService
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val orderService: OrderService,
    private val paymentService: PaymentService
):ViewModel() {
    private val _placedOrderState = MutableStateFlow(PlacedOrderState())
    val placedOrderState = _placedOrderState.asStateFlow()
    private val _makePaymentState = MutableStateFlow(MakePaymentState())
    val makePaymentState = _makePaymentState.asStateFlow()

    suspend fun placeOrder(placeOrderDto: PlaceOrderDto, paymentId: String):Boolean{
        try {
            val TOKEN = sharedPreferences.getString(TOKENPREF,"")

            if(TOKEN != null) {
                Log.d("USER TOKEN :-> ",TOKEN)
                val token = "Bearer ${TOKEN}"
                _placedOrderState.value = PlacedOrderState(isLoading = true)
                val result = orderService.placeOrder(token, placeOrderDto)

                Log.d("VANDAN", "$result")

                _placedOrderState.value =
                    PlacedOrderState(placeOrderResponseDto = result)

                val makePaymentRequest = MakePaymentRequest(
                    orderId = result.order.id,
                    paymentId = paymentId,
                    paymentMethod = "UPI",
                    paymentStatus = "COMPLETED",
                    shopId = result.order.shopId,
                    foodCourtId = result.order.foodCourtId,
                    totalAmount = placeOrderDto.totalAmount
                )

                _makePaymentState.value = MakePaymentState(isLoading = true)
                val paymentResponse = paymentService.makePayment(token = token, makePaymentRequest = makePaymentRequest)
                Log.d("Payement Response", "$paymentResponse")

                _makePaymentState.value = MakePaymentState(data = paymentResponse, isLoading = false)
                _placedOrderState.value = PlacedOrderState(isLoading = false)
                return true
            }else{
                throw Exception("Token is null")
            }

        }catch (e:Exception){
            Log.d("VANDAN", "${e}")
            _placedOrderState.value = PlacedOrderState(error = e.message, isLoading = false)
            _makePaymentState.value = MakePaymentState(error = e.message, isLoading = false)
            return false
        }finally {

        }

    }




}
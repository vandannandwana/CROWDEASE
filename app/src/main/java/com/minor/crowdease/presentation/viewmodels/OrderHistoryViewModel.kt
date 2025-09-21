package com.minor.crowdease.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor.crowdease.data.dto.make_payment.MakePaymentRequest
import com.minor.crowdease.data.dto.make_payment.MakePaymentState
import com.minor.crowdease.data.dto.order_history.OrderHistoryState
import com.minor.crowdease.data.dto.order_history_info.OrderHistoryInfoDto
import com.minor.crowdease.data.dto.order_history_info.OrderHistoryInfoState
import com.minor.crowdease.data.dto.order_place.PlacedOrderState
import com.minor.crowdease.data.remote.OrderHistoryService
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val orderHistoryService: OrderHistoryService
) : ViewModel() {
    private var _orderHistoryState = MutableStateFlow(OrderHistoryState())
    val orderHistoryState = _orderHistoryState.asStateFlow()

    var _orderHistoryInfoState = MutableStateFlow(OrderHistoryInfoState())
    val orderHistoryInfoState = _orderHistoryInfoState.asStateFlow()

    init {
        viewModelScope.launch {
            getOrderHistory()
        }
    }

    suspend fun getOrderHistory() {
        val TOKEN = sharedPreferences.getString(TOKENPREF, "")
        if (TOKEN != null) {
            try {
                Log.d("USER TOKEN :-> ", TOKEN)
                val token = "Bearer $TOKEN"
                _orderHistoryState.value = OrderHistoryState(isLoading = true)
                val result = orderHistoryService.getOrderHistory(token)

                Log.d("ORDER_HISTORY_RESPONSE", "$result")

                _orderHistoryState.value = OrderHistoryState(
                    orderHistory = result,
                    isLoading = false
                )
            } catch (e: Exception) {
                _orderHistoryState.value = OrderHistoryState(error = e.message, isLoading = false)
            }
        } else {
            throw Exception("Token is null")
        }
    }

    suspend fun getOrderHistoryInfo(orderID: String) {
        delay(1000)
        val TOKEN = sharedPreferences.getString(TOKENPREF, "")
        if (TOKEN != null) {
            try {
                Log.d("USER TOKEN :-> ", TOKEN)
                val token = "Bearer ${TOKEN}"
                _orderHistoryInfoState.value = OrderHistoryInfoState(isLoading = true)
                val result = orderHistoryService.getOrderHistoryInfo(token, orderID)

                Log.d("ORDER_HISTORY_RESPONSE", "$result")

                _orderHistoryInfoState.value = OrderHistoryInfoState(
                    orderHistoryInfo = result,
                    isLoading = false
                )
            } catch (e: Exception) {
                _orderHistoryInfoState.value =
                    OrderHistoryInfoState(error = e.message, isLoading = false)
            }
        } else {
            throw Exception("Token is null")
        }
    }

}
package com.minor.crowdease.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.dto.order_place.main.PlaceOrderDto
import com.minor.crowdease.data.dto.order_place.PlacedOrderState
import com.minor.crowdease.data.remote.OrderService
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val orderService: OrderService
):ViewModel() {


    private val _placedOrderState = MutableStateFlow(PlacedOrderState())
    val placedOrderState = _placedOrderState.asStateFlow()



    suspend fun placeOrder(placeOrderDto: PlaceOrderDto):Boolean{

        try {

            val TOKEN = sharedPreferences.getString(TOKENPREF,"")

            if(TOKEN != null) {
                Log.d("USER TOKEN :-> ",TOKEN)
                val token = "Bearer ${TOKEN}"

                _placedOrderState.value = PlacedOrderState(isLoading = true)

                val result = orderService.placeOrder(token, placeOrderDto)
                Log.d("VANDAN", "$result")
                _placedOrderState.value =
                    PlacedOrderState(placeOrderResponseDto = result, isLoading = false)
                return true
            }else{
                throw Exception("Token is null")
            }

        }catch (e:Exception){
            Log.d("VANDAN", "${e}")
            _placedOrderState.value = PlacedOrderState(error = e.message, isLoading = false)
            return false
        }

    }




}
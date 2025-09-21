package com.minor.crowdease.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor.crowdease.data.remote.FoodCourtService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val courtService: FoodCourtService
) : ViewModel() {


    private val pendingOrdersMap = mutableMapOf<String, MutableStateFlow<Int>>()

    fun getPendingOrdersState(foodCourtId: String): StateFlow<Int> {
        return pendingOrdersMap.getOrPut(foodCourtId) {
            MutableStateFlow(0).also { flow ->
                startPolling(foodCourtId, flow)  // Start polling when the state is first created
            }
        }
    }

    private fun startPolling(foodCourtId: String, flow: MutableStateFlow<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    val orders = courtService.pendingOrderFoodCourt(foodCourtId) // Fetch from API/DB
                    Log.d("PENDING ORDERS", "Polling $foodCourtId: ${orders.pendingOrders}")
                    flow.emit(orders.pendingOrders) // Update flow with new data
                } catch (e: Exception) {
                    Log.e("ERROR", "Error fetching pending orders", e)
                }
                delay(25000)
            }
        }
    }
}






//    suspend fun getPendingOrders(courtId: String): Int {
//        try {
//            val response = courtService.pendingOrderFoodCourt(courtId)
//            return response.pendingOrders
//        }catch (e:Exception){
//            Log.d("Pending Orders", "getPendingOrders: ${e.message}")
//            return 0
//        }
//    }
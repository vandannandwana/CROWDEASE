package com.minor.crowdease.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.remote.FoodCourtService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val courtService: FoodCourtService
):ViewModel() {


    suspend fun getPendingOrders(courtId: String): Int {
        try {
            val response = courtService.pendingOrderFoodCourt(courtId)
            return response.pendingOrders
        }catch (e:Exception){
            Log.d("Pending Orders", "getPendingOrders: ${e.message}")
            return 0
        }
    }


}
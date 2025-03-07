package com.minor.crowdease.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor.crowdease.data.dto.food_court.FoodCourtDto
import com.minor.crowdease.data.dto.food_court.FoodCourtState
import com.minor.crowdease.data.remote.FoodCourtService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodCourtViewModel @Inject constructor(
    private val foodCourtService: FoodCourtService
) : ViewModel() {

    // Main state for food courts and their pending orders
    private val _foodCourtState = MutableStateFlow(FoodCourtState())
    val foodCourtState: StateFlow<FoodCourtState> = _foodCourtState.asStateFlow()

    // Cache for pending orders per food court ID
    private val pendingOrdersMap = mutableMapOf<String, MutableStateFlow<PendingOrderState>>()

    init {
        fetchFoodCourts()
    }

    // Fetch or refresh food courts
    fun fetchFoodCourts() {
        viewModelScope.launch {
            try {
                _foodCourtState.update { it.copy(isLoading = true) }
                val foodCourts = foodCourtService.getFoodCourts()
                _foodCourtState.update {
                    it.copy(
                        foodCourts = foodCourts,
                        isLoading = false,
                        error = null
                    )
                }
                // Fetch pending orders for all food courts after loading
                foodCourts.data.forEach { foodCourt ->
                    fetchPendingOrders(foodCourt.id)
                }
            } catch (e: Exception) {
                _foodCourtState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load food courts"
                    )
                }
            }
        }
    }

    // Get pending orders as a StateFlow for a specific food court
    fun getPendingOrdersState(foodCourtId: String): StateFlow<PendingOrderState> {
        return pendingOrdersMap.getOrPut(foodCourtId) {
            MutableStateFlow(PendingOrderState()).also { flow ->
                fetchPendingOrders(foodCourtId)
            }
        }
    }

    // Fetch pending orders for a specific food court
    private fun fetchPendingOrders(foodCourtId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val flow = pendingOrdersMap[foodCourtId] ?: return@launch
            try {
                flow.update { it.copy(isLoading = true) }
                val response = foodCourtService.pendingOrderFoodCourt(foodCourtId)
                flow.update {
                    it.copy(
                        pendingOrders = response.pendingOrders,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.d("PendingOrders", "fetchPendingOrders: ${e.message}")
                flow.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load pending orders"
                    )
                }
            }
        }
    }

    // Optional: Refresh pending orders manually
    fun refreshPendingOrders(foodCourtId: String) {
        fetchPendingOrders(foodCourtId)
    }
}

// Updated state classes
data class FoodCourtState(
    val isLoading: Boolean = false,
    val foodCourts: FoodCourtDto? = null,
    val error: String? = null
)

data class PendingOrderState(
    val pendingOrders: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
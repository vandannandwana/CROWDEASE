package com.minor.crowdease.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.minor.crowdease.data.dto.food_court.FoodData
import com.minor.crowdease.data.dto.food_court.FoodDataState
import com.minor.crowdease.data.dto.food_court.FoodDto
import com.minor.crowdease.data.remote.ShopService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val shopService: ShopService,
) : ViewModel() {

    // Use StateFlow for selected items (ensure FoodData is Parcelable or Serializable)
    private val _selectedItems = MutableStateFlow(
        savedStateHandle.get<Map<FoodData, Int>>("selectedItem") ?: emptyMap()
    )
    val selectedItems: StateFlow<Map<FoodData, Int>> = _selectedItems.asStateFlow()

    private val _foodDataState = MutableStateFlow(FoodDataState())
    val foodDataState: StateFlow<FoodDataState> = _foodDataState.asStateFlow()

    fun clearCart() {
        _selectedItems.value = emptyMap()
        savedStateHandle["selectedItem"] = emptyMap<FoodData, Int>()
    }

    suspend fun getFoodData(shopId: String) {
        try {
            _foodDataState.value = _foodDataState.value.copy(isLoading = true)
            val foodData = shopService.getMenu(shopId)
            Log.d("VANDAN FOOD", "getFoodData: $foodData")
            _foodDataState.value = _foodDataState.value.copy(foodData = foodData, isLoading = false)
        } catch (e: Exception) {
            Log.d("VANDAN FOOD", "getFoodData: ${e.message}")
            _foodDataState.value = _foodDataState.value.copy(error = e.message.toString(), isLoading = false)
        }
    }

    fun addItem(foodData: FoodData) {
        val updatedMap = _selectedItems.value.toMutableMap()
        updatedMap[foodData] = updatedMap.getOrDefault(foodData, 0) + 1
        _selectedItems.value = updatedMap // Update StateFlow
        savedStateHandle["selectedItem"] = updatedMap.toMap() // Save a copy to StateHandle
    }

    fun removeItem(foodData: FoodData) {
        val updatedMap = _selectedItems.value.toMutableMap()

        if (updatedMap.containsKey(foodData)) {
            if (updatedMap[foodData] == 1) {
                updatedMap.remove(foodData)
            } else {
                updatedMap[foodData] = updatedMap[foodData]!! - 1
            }
        }

        _selectedItems.value = updatedMap // Update StateFlow
        savedStateHandle["selectedItem"] = updatedMap.toMap() // Save a copy to StateHandle
    }
}

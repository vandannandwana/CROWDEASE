package com.minor.crowdease.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minor.crowdease.data.dto.food_court.FoodCourtState
import com.minor.crowdease.data.remote.FoodCourtService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodCourtViewModel @Inject constructor(
    private val foodCourtService: FoodCourtService
):ViewModel() {

    private val _foodCourtState = MutableStateFlow(FoodCourtState())

    val foodCourtState = _foodCourtState.asStateFlow()


    init {
        viewModelScope.launch {
            getFoodCourts()
        }
    }


    private suspend fun getFoodCourts(){
        try {
            _foodCourtState.value = _foodCourtState.value.copy(isLoading = true)

            val foodCourts = foodCourtService.getFoodCourts()

            _foodCourtState.value = _foodCourtState.value.copy(
                foodCourts = foodCourts,
                isLoading = false
            )
        }catch (e:Exception){
            _foodCourtState.value = _foodCourtState.value.copy(
                error = e.message,
                isLoading = false
            )
        }
    }



}
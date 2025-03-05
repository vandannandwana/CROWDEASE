package com.minor.crowdease.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.dto.shop.ShopState
import com.minor.crowdease.data.remote.ShopService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private  val shopService: ShopService
):ViewModel() {

    private val _shopState = MutableStateFlow(ShopState())

    val shopState = _shopState.asStateFlow()

    suspend fun getShops(foodCourtId: String){
        try {
            _shopState.value = _shopState.value.copy(isLoading = true)

            val result = shopService.getShops(foodCourtId)
            _shopState.value = _shopState.value.copy(
                shopCourts = result,
                isLoading = false
            )
        }catch (e:Exception){
            _shopState.value = _shopState.value.copy(
                error = e.message,
                isLoading = false
            )
        }

    }


    suspend fun getPendingOrders(shopId: String): Int {
        try {
            val response = shopService.getPendingOrders(shopId)
            return response.pendingOrders
        }catch (e:Exception){
            Log.d("Pending Orders", "getPendingOrders: ${e.message}")
            return 0
        }
    }







}
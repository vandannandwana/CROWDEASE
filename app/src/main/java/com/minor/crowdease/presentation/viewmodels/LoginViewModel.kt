package com.minor.crowdease.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.dto.food_court.LoginRequest
import com.minor.crowdease.data.dto.food_court.LoginState
import com.minor.crowdease.data.dto.food_court.RegisterRequest
import com.minor.crowdease.data.dto.food_court.RegisterState
import com.minor.crowdease.data.remote.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService
):ViewModel(){

    val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()
    val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()




    suspend fun login(email:String,password:String):Boolean{

        try{
            _loginState.value = LoginState(isLoading = true)

            val result = loginService.login(LoginRequest(email, password))

            _loginState.value = LoginState(loginResponse = result, isLoading = false)
            return true
        }catch (e:Exception){
            _loginState.value = LoginState(error = e.message, isLoading = false)
            return false
        }

    }

    suspend fun register(name:String,email:String,password:String,phoneNumber:String):Boolean{

        try {
            _registerState.value = RegisterState(isLoading = true)

            val result = loginService.register(RegisterRequest(name, email, password, phoneNumber))

            _registerState.value = RegisterState(loginResponse = result, isLoading = false)
            return true
        }catch (e:Exception){

            _registerState.value = RegisterState(error = e.message, isLoading = false)
            return false
        }

    }







}
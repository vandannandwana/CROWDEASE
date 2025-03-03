package com.minor.crowdease.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.minor.crowdease.data.dto.login.LoginRequest
import com.minor.crowdease.data.dto.login.LoginState
import com.minor.crowdease.data.dto.login.OtpRequest
import com.minor.crowdease.data.dto.login.RegisterRequest
import com.minor.crowdease.data.dto.login.RegisterState
import com.minor.crowdease.data.remote.LoginService
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService,
    private val tokenPreferences: SharedPreferences
):ViewModel(){

    val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()
    val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    suspend fun login(email:String,password:String):Boolean{

        try{
            _loginState.value = LoginState(isLoading = true)

            delay(3000)

            val result = loginService.login(LoginRequest(email, password))

            val editor = tokenPreferences.edit()
            editor.putString(TOKENPREF,result.token)
            editor.apply()

            Log.d("VANDAN LOGIN",result.toString())
            _loginState.value = LoginState(loginResponse = result, isLoading = false)
            return true
        }catch (e:Exception){
            _loginState.value = LoginState(error = e.message, isLoading = false)
            Log.d("VANDAN LOGIN",e.toString())
            return false
        }

    }

    suspend fun register(name:String,email:String,password:String,phoneNumber:String):Boolean{

        try {
            _registerState.value = RegisterState(isLoading = true)

            delay(3000)

            val result = loginService.register(RegisterRequest(name, email, password, phoneNumber))

            val editor = tokenPreferences.edit()
            editor.putString(TOKENPREF,result.token)
            editor.apply()

            Log.d("VANDAN LOGIN",result.toString())
            _registerState.value = RegisterState(loginResponse = result, isLoading = false)
            return true
        }catch (e:Exception){
            Log.d("VANDAN LOGIN",e.toString())
            _registerState.value = RegisterState(error = e.message, isLoading = false)
            return false
        }

    }

    suspend fun verify(otp:String):Boolean{

        try {

            val token = tokenPreferences.getString(TOKENPREF,null)
            if (token != null){
                val result  = loginService.verify("barrier $token", OtpRequest(otp))
                Log.d("VANDAN VERIFY",result.toString())
                return true
            }else{
                Log.d("VANDAN VERIFY","Token is null")
                return false
            }
        }catch (e: Exception){
            Log.d("VANDAN VERIFY",e.toString())
            return false
        }

    }







}
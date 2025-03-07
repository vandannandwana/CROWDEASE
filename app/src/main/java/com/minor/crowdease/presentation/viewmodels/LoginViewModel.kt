package com.minor.crowdease.presentation.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.minor.crowdease.data.dto.login.LoginRequest
import com.minor.crowdease.data.dto.login.LoginState
import com.minor.crowdease.data.dto.login.OtpRequest
import com.minor.crowdease.data.dto.login.RegisterRequest
import com.minor.crowdease.data.dto.login.RegisterState
import com.minor.crowdease.data.dto.user.UpdateState
import com.minor.crowdease.data.dto.user.UserDto
import com.minor.crowdease.data.remote.LoginService
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.utlis.Constants.Companion.STUDENT_EMAIL
import com.minor.crowdease.utlis.Constants.Companion.STUDENT_NAME
import com.minor.crowdease.utlis.Constants.Companion.STUDENT_PHONE_NUMBER
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService,
    private val tokenPreferences: SharedPreferences
):ViewModel(){

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _updateState = MutableStateFlow(UpdateState())
    val updateState = _updateState.asStateFlow()


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
            getUserData(result.token)
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

            Log.d("VANDAN REGISTER",result.toString())
            _registerState.value = RegisterState(loginResponse = result, isLoading = false)
            getUserData(token = result.token)
            return true
        }catch (e:Exception){
            Log.d("VANDAN REGISTER",e.toString())
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

    suspend fun getUserData(token: String): UserDto? {

        try {
                val result = loginService.getUserData(token = "barrier $token")

                Log.d("VANDAN USERDATA",result.toString())
                Log.d("VANDAN USERDATA",result.student.toString())

                val editor = tokenPreferences.edit()
                editor.putString(STUDENT_NAME,result.student.name)
                editor.putString(STUDENT_EMAIL,result.student.email)
                editor.putString(STUDENT_PHONE_NUMBER,result.student.phoneNumber)
                editor.apply()
            return result
        }catch (e:Exception){

            Log.d("VANDAN USERDATA",e.toString())
            return null
        }

    }

    fun logout(navHostController: NavHostController){

        val editor = tokenPreferences.edit()
        editor.clear()
        editor.apply()
        navHostController.popBackStack()
        navHostController.navigate(Screens.LoginScreen.route)

    }


    suspend fun updateUser(context:Context,token:String,userData: UserDto,imageUri:Uri?){
        _updateState.value = _updateState.value.copy(
            isUpdating = true
        )
        if (imageUri != null) {

            val file = getFileFromUri(context = context, imageUri)

            if (file != null) {
                try {
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val image = MultipartBody.Part.createFormData("profile", file.name, requestFile)
                    val name =
                        RequestBody.create("text/plain".toMediaTypeOrNull(), userData.student.name)
                    val phoneNumber = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        userData.student.phoneNumber
                    )
                    loginService.updateUserData(token = "barrier $token", name, phoneNumber, image)
                    _updateState.value = _updateState.value.copy(
                        isUpdated = true,
                        isUpdating = false
                    )
                    Log.d("VANDAN UPDATE", "File is not null")
                }catch (e:Exception){
                    Log.d("VANDAN UPDATE",e.toString())
                    _updateState.value = _updateState.value.copy(
                        error = e.message,
                        isUpdating = false
                    )
                }
            } else {
                Log.d("VANDAN UPDATE", "File is null")
                _updateState.value = _updateState.value.copy(
                    error = "File is null",
                    isUpdating = false
                )
            }
        }else{
            Log.d("VANDAN UPDATE","uri is null")
            _updateState.value = _updateState.value.copy(
                error = "Image is null",
                isUpdating = false
            )
        }

    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val name = cursor?.let {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: return null

        val file = File(context.cacheDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun clearLoginError() {
        _loginState.value = _loginState.value.copy(error = null)
    }

    fun clearRegisterError() {
        _registerState.value = _registerState.value.copy(error = null)
    }


}
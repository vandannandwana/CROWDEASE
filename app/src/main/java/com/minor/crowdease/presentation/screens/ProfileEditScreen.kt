package com.minor.crowdease.presentation.screens

import android.content.Context
import android.net.Uri
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.data.dto.user.Student
import com.minor.crowdease.data.dto.user.UserDto
import com.minor.crowdease.presentation.viewmodels.LoginViewModel
import com.minor.crowdease.utlis.Constants
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import kotlinx.coroutines.launch

@Composable
fun ProfileEditScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    var updateState = loginViewModel.updateState.collectAsStateWithLifecycle().value
    LaunchedEffect(updateState){
        if (updateState.isUpdated){
            Toast.makeText(
                context,
                "Profile Updated Successfully",
                Toast.LENGTH_LONG
            ).show()
            updateState = updateState.copy(isUpdated = false)
        }
    }
    val buttonSize by animateFloatAsState(
        if(updateState.isUpdating) 0f else 1f,
    )

    Scaffold(
        modifier= Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            val sharedPreferences = context.getSharedPreferences(TOKENPREF, Context.MODE_PRIVATE)
            val token  = sharedPreferences.getString(TOKENPREF,null)
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


            var userInfoState by remember {
                mutableStateOf<UserDto?>(null)
            }

            var userData by remember {
                mutableStateOf(UserDto("",Student("","","","")))
            }

            val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    selectedImageUri = it
                    userData.student.image = it.toString()
                }
            }
            LaunchedEffect(token){
                if(token != null){
                    userInfoState = loginViewModel.getUserData(token)
                }
            }

            LaunchedEffect(userInfoState){
                if(userInfoState != null){
                    userData = userInfoState!!
                }
            }

            Text(
                text = "Edit Profile",
                fontFamily = Constants.POOPINS_FONT_BOLD
            )

            if(userInfoState!= null){

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center

                ){
                    AsyncImage(
                        model = if(selectedImageUri == null) userInfoState!!.student.image else selectedImageUri,
                        contentDescription = "",
                        modifier = Modifier.size(170.dp)
                            .border(2.dp, colorResource(Constants.BLUE_COLOR), shape = CircleShape)
                            .padding(8.dp)
                            .alpha(0.5f)
                            .clip(CircleShape)
                            .clickable {
                                imagePicker.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.ModeEdit,
                        contentDescription = "Edit Icon",
                        tint = colorResource(Constants.TEXT_COLOR),
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier= Modifier.height(24.dp))


                OutlinedTextField(
                    value = userData.student.name,
                    onValueChange = {name->
                        userData = userData.copy(
                            student = userData.student.copy(
                                name = name
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Name", color = colorResource(Constants.TEXT_COLOR)) },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = userData.student.email,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Email", color = colorResource(Constants.TEXT_COLOR)) },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = userData.student.phoneNumber,
                    onValueChange = {phoneNumber->
                        userData = userData.copy(
                            student = userData.student.copy(
                                phoneNumber = phoneNumber
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("PhoneNumber", color = colorResource(Constants.TEXT_COLOR)) },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    ),
                    singleLine = true
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    if (buttonSize > 0f) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(buttonSize)
                                .padding(12.dp),
                            shape = RoundedCornerShape(7.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(Constants.BLUE_COLOR)
                            ),
                            onClick = {
                                scope.launch {
                                    if (token != null) {
                                        loginViewModel.updateUser(
                                            token = token,
                                            userData = userData,
                                            context = context,
                                            imageUri = selectedImageUri
                                        )
                                    }
                                }
                            }
                        ) {
                            Text(text = "Update Profile")
                        }
                    } else {
                        CircularProgressIndicator()
                        if (updateState.error != null) {
                            Toast.makeText(
                                context,
                                updateState.error,
                                Toast.LENGTH_LONG
                            ).show()
                            updateState = updateState.copy(error = null)
                        }
                    }
                }

            }else{
                CircularProgressIndicator()
            }






        }

    }

}
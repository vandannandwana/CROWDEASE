package com.minor.crowdease.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.LoginViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch


@Composable
fun OtpScreen(navHostController: NavHostController) {

    val loginViewModel = hiltViewModel<LoginViewModel>()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var otp by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(otp){

        if(otp.length >=6){

            keyboardController?.hide()

        }

    }


    Scaffold {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                .padding(24.dp)
            ){

                Text(
                    text = "Otp verification",
                    fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                    fontSize = 32.sp
                )

                Image(
                    painter = painterResource(R.drawable.otp_icon),
                    contentDescription = "otp_icon",
                    modifier = Modifier.size(96.dp)
                )


                TextField(
                    value = otp,
                    onValueChange = { otp = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text("Enter OTP", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 24.sp, color = Color.Gray) },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        textAlign = TextAlign.Center,
                        fontSize = 34.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(Constants.ORANGE_COLOR)
                    ),
                    onClick = {
                        scope.launch {
                            if(loginViewModel.verify(otp)){
                                navHostController.navigate(Screens.MainScreen.route)
                                Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                ) {
                    Text("Verify")
                }


            }




        }

    }

}

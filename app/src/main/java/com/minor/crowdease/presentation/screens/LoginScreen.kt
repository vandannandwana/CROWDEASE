package com.minor.crowdease.presentation.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.LoginViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navHostController:NavHostController,modifier: Modifier = Modifier) {

    val loginViewModel = hiltViewModel<LoginViewModel>()

    val scope = rememberCoroutineScope()

    val loginState = loginViewModel.loginState.collectAsStateWithLifecycle().value
    val registerState = loginViewModel.registerState.collectAsStateWithLifecycle().value

    val blue_color = Color(0xFF2563E9)

    var currentPage by remember {
        mutableStateOf(LS.LOGIN)
    }

    val context = LocalContext.current

    val dynamicColorLogin by animateColorAsState(
        targetValue = if (currentPage == LS.LOGIN) Color.White else Color.LightGray,
        tween(200)
    )

    val fontColorLogin by animateColorAsState(
        targetValue = if (currentPage == LS.LOGIN) blue_color else Color.Black,
        tween(200)
    )

    val dynamicColorRegister by animateColorAsState(
        targetValue = if (currentPage == LS.SIGNUP) Color.White else Color.LightGray,
        tween(200)
    )

    val fontColorRegister by animateColorAsState(
        targetValue = if (currentPage == LS.SIGNUP) blue_color else Color.Black,
        tween(200)
    )

    val name_height by animateDpAsState(
        targetValue = if (currentPage == LS.LOGIN) 0.dp else 68.dp,
        tween(200)
    )

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    var re_password by rememberSaveable {
        mutableStateOf("")
    }

    var name by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { inp ->
        val ip = inp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip)
                .background(Color(0xFFEFF6FD))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xFF2563EB)),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = "CROWDEASE",
                    fontFamily = Constants.PROTEST_FONT,
                    color = Color.White
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                //Login Register Box
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(7.dp))
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(dynamicColorLogin)
                                .clickable { currentPage = LS.LOGIN },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Login",
                                modifier = Modifier.padding(12.dp),
                                color = fontColorLogin,
                                fontFamily = Constants.POOPINS_FONT_SEMI_BOLD
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(dynamicColorRegister)
                                .clickable { currentPage = LS.SIGNUP },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Register",
                                modifier = Modifier.padding(12.dp),
                                color = fontColorRegister,
                                fontFamily = Constants.POOPINS_FONT_SEMI_BOLD
                            )
                        }
                    }

                }

                //Text Fields Section
                if (currentPage == LS.SIGNUP) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .height(name_height),
                        label = { Text("Full Name") },
                        textStyle = TextStyle(
                            fontFamily = Constants.POOPINS_FONT_REGULAR
                        ),
                        singleLine = true
                    )
                }


                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Email") },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Password") },
                    textStyle = TextStyle(
                        fontFamily = Constants.POOPINS_FONT_REGULAR
                    ),
                    singleLine = true
                )

                if (currentPage == LS.SIGNUP) {
                    OutlinedTextField(
                        value = re_password,
                        onValueChange = { re_password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .height(name_height),
                        label = { Text("Re-Password") },
                        textStyle = TextStyle(
                            fontFamily = Constants.POOPINS_FONT_REGULAR
                        ),
                        singleLine = true
                    )
                }

                Text(
                    "Forgot password?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.End,
                    color = blue_color
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue_color
                    ),
                    onClick = {
                        if (currentPage == LS.LOGIN) {
                            scope.launch {
                                if(loginViewModel.login(email, password)){
                                    navHostController.navigate(Screens.MainScreen.route)
                                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(context, loginState.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            scope.launch {
                                if (loginViewModel.register(name, email, password, "1234567890")){
                                    navHostController.navigate(Screens.OtpScreen.route)
                                    Toast.makeText(context, "Otp Sent Successfully", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(context, loginState.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        navHostController.navigate(Screens.MainScreen.route)
                        Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text(if (currentPage == LS.LOGIN) "Login" else "Register")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF878181))
                    )
                    Text(
                        " Or continue with ",
                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                        modifier = Modifier.background(Color.White)
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(7.dp),
                    onClick = {
                        Toast.makeText(context, "Google Sign in Done", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google_ic),
                        contentDescription = "google_icon",
                        modifier = Modifier.size(34.dp)
                    )
                }

                Text(
                    text = buildAnnotatedString {
                        append("By Continuing, you agree to our ")
                        withStyle(style = SpanStyle(color = blue_color)) {
                            append("Terms of Service")
                        }
                        append(" and ")
                        withStyle(style = SpanStyle(color = blue_color)) {
                            append("Privacy Policy ")
                        }
                    },
                    fontFamily = Constants.POOPINS_FONT_REGULAR,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )

            }

        }

    }

}


enum class LS {
    LOGIN,
    SIGNUP
}
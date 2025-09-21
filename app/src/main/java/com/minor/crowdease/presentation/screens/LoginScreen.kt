package com.minor.crowdease.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.data.dto.login.LoginState
import com.minor.crowdease.data.dto.login.RegisterState
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.LoginViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch

enum class LS {
    LOGIN,
    SIGNUP
}

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val registerState by loginViewModel.registerState.collectAsStateWithLifecycle()

    // Theme colors
    val buttonColor = colorResource(id = Constants.ORANGE_COLOR)
    val backgroundColor = colorResource(id = Constants.BACKGROUND_COLOR)
    val textColor = colorResource(id = Constants.TEXT_COLOR)
    val button_textColor = colorResource(id = Constants.BUTTON_TEXT_COLOR)

    // State
    var currentPage by rememberSaveable { mutableStateOf(LS.LOGIN) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rePassword by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }

    // Animations
    val buttonScale by animateFloatAsState(
        targetValue = if (loginState.isLoading || registerState.isLoading) 0f else 1f,
        animationSpec = tween(200)
    )
    val nameFieldHeight by animateDpAsState(
        targetValue = if (currentPage == LS.LOGIN) 0.dp else 68.dp,
        animationSpec = tween(200)
    )

    // Dynamic colors for tabs
    val (loginTabBackground, loginTabText) = tabColors(currentPage == LS.LOGIN, buttonColor)
    val (signupTabBackground, signupTabText) = tabColors(currentPage == LS.SIGNUP, buttonColor)

    Scaffold(
        modifier = modifier.fillMaxSize()
            .background(backgroundColor),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            LogoBox()

            // Login/Signup Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                // Tabs
                LoginSignupTabs(
                    currentPage = currentPage,
                    onPageChange = { currentPage = it },
                    loginTabBackground = loginTabBackground,
                    loginTabText = loginTabText,
                    signupTabBackground = signupTabBackground,
                    signupTabText = signupTabText
                )

                // Form Fields
                LoginSignupForm(
                    currentPage = currentPage,
                    name = name,
                    onNameChange = { name = it },
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    rePassword = rePassword,
                    onRePasswordChange = { rePassword = it },
                    nameFieldHeight = nameFieldHeight,
                    textColor = textColor
                )

                // Action Button
                ActionButton(
                    currentPage = currentPage,
                    buttonScale = buttonScale,
                    blueColor = buttonColor,
                    textColor = button_textColor,
                    loginState = loginState,
                    registerState = registerState,
                    onLoginClick = {
                        scope.launch {
                            if (loginViewModel.login(email, password)) {
                                navHostController.navigate(Screens.MainScreen.route)
                                showToast(context, "Login Successful")
                            }
                        }
                    },
                    onRegisterClick = {
                        scope.launch {
                            if (loginViewModel.register(name, email, password, "1234567890")) {
                                navHostController.navigate(Screens.OtpScreen.route)
                                showToast(context, "OTP Sent Successfully")
                            }
                        }
                    }
                )

                // Social Login and Terms
                SocialLoginAndTerms(blueColor = buttonColor, textColor = textColor)
            }
        }
    }

    // Error Handling
    loginState.error?.let { error ->
        LaunchedEffect(error) {
            showToast(context, error)
            loginViewModel.clearLoginError()
        }
    }
    registerState.error?.let { error ->
        LaunchedEffect(error) {
            showToast(context, error)
            loginViewModel.clearRegisterError()
        }
    }
}

// Reusable Components
@Composable
private fun LogoBox() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(colorResource(id = Constants.ORANGE_COLOR)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CROWDEASE",
            fontFamily = Constants.PROTEST_FONT,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun LoginSignupTabs(
    currentPage: LS,
    onPageChange: (LS) -> Unit,
    loginTabBackground: Color,
    loginTabText: Color,
    signupTabBackground: Color,
    signupTabText: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.Center
    ) {
        TabButton(
            text = "Login",
            isSelected = currentPage == LS.LOGIN,
            backgroundColor = loginTabBackground,
            textColor = loginTabText,
            onClick = { onPageChange(LS.LOGIN) },
            modifier = Modifier.weight(0.5f)
        )
        TabButton(
            text = "Register",
            isSelected = currentPage == LS.SIGNUP,
            backgroundColor = signupTabBackground,
            textColor = signupTabText,
            onClick = { onPageChange(LS.SIGNUP) },
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            color = textColor,
            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD
        )
    }
}

@Composable
private fun LoginSignupForm(
    currentPage: LS,
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    rePassword: String,
    onRePasswordChange: (String) -> Unit,
    nameFieldHeight: Dp,
    textColor: Color
) {
    if (currentPage == LS.SIGNUP) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(nameFieldHeight),
            label = { Text("Full Name", color = textColor) },
            textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
            singleLine = true
        )
    }
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        label = { Text("Email", color = textColor) },
        textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        label = { Text("Password", color = textColor) },
        textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    if (currentPage == LS.SIGNUP) {
        OutlinedTextField(
            value = rePassword,
            onValueChange = onRePasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(nameFieldHeight),
            label = { Text("Re-Password", color = textColor) },
            textStyle = TextStyle(fontFamily = Constants.POOPINS_FONT_REGULAR),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    Text(
        text = "Forgot password?",
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        textAlign = TextAlign.End,
        color = textColor
    )
}

@Composable
private fun ActionButton(
    currentPage: LS,
    buttonScale: Float,
    blueColor: Color,
    textColor: Color,
    loginState: LoginState, // Assume this is your state class
    registerState: RegisterState, // Assume this is your state class
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (buttonScale > 0f) {
            Button(
                onClick = if (currentPage == LS.LOGIN) onLoginClick else onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth(buttonScale)
                    .padding(12.dp),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(containerColor = blueColor)
            ) {
                Text(
                    text = if (currentPage == LS.LOGIN) "Login" else "Register",
                    color = textColor
                )
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SocialLoginAndTerms(blueColor: Color, textColor: Color) {
    DividerWithText(text = "Or continue with", color = textColor)
    OutlinedButton(
        onClick = { /* Handle Google Sign-In */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(7.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.google_ic),
            contentDescription = "Google Sign-In",
            modifier = Modifier.size(34.dp)
        )
    }
    TermsText(blueColor = blueColor, textColor = textColor)
}

// Helper Functions
@Composable
private fun DividerWithText(text: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Divider(color = color)
        Text(
            text = text,
            fontFamily = Constants.POOPINS_FONT_REGULAR,
            modifier = Modifier
                .background(colorResource(id = Constants.BACKGROUND_COLOR))
                .padding(horizontal = 8.dp),
            color = color
        )
    }
}

@Composable
private fun TermsText(blueColor: Color, textColor: Color) {
    Text(
        text = buildAnnotatedString {
            append("By continuing, you agree to our ")
            withStyle(style = SpanStyle(color = blueColor)) {
                append("Terms of Service")
            }
            append(" and ")
            withStyle(style = SpanStyle(color = blueColor)) {
                append("Privacy Policy")
            }
        },
        fontFamily = Constants.POOPINS_FONT_REGULAR,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        color = textColor
    )
}

@Composable
private fun tabColors(isSelected: Boolean, blueColor: Color): Pair<Color, Color> {
    val background by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.LightGray,
        animationSpec = tween(200)
    )
    val text by animateColorAsState(
        targetValue = if (isSelected) blueColor else Color.Black,
        animationSpec = tween(200)
    )
    return background to text
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

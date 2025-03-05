package com.minor.crowdease.presentation.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.minor.crowdease.R
import com.minor.crowdease.data.dto.user.Student
import com.minor.crowdease.data.dto.user.UserDto
import com.minor.crowdease.navigations.Screens
import com.minor.crowdease.presentation.viewmodels.LoginViewModel
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {

    ProfileScreenPreview(navHostController = navHostController)

}

@Composable
private fun ProfileScreenPreview(navHostController:NavHostController) {

    val context = LocalContext.current
    val sharedPreferences =context.getSharedPreferences(Constants.TOKENPREF, Context.MODE_PRIVATE)
    val token = sharedPreferences.getString(Constants.TOKENPREF,"")
    val name = sharedPreferences.getString(Constants.STUDENT_NAME,"")
    val email = sharedPreferences.getString(Constants.STUDENT_EMAIL,"")
    val phoneNumber = sharedPreferences.getString(Constants.STUDENT_PHONE_NUMBER,"")
    var userData by remember {
        mutableStateOf(Student("","","",""))
    }

    val loginViewModel = hiltViewModel<LoginViewModel>()

    LaunchedEffect(token){
        if(token !=null){
            val userState  = loginViewModel.getUserData(token)
            if(userState != null){
                userData = userState.student
            }
        }
    }



    val scope = rememberCoroutineScope()

    var dialogBoxShow by remember {
        mutableStateOf(false)
    }

    val settings = listOf(
        Setting(icon = Icons.Default.Person,"Edit Profile",{
            navHostController.navigate(Screens.ProfileEditScreen.route)
        }),
        Setting(icon = Icons.Default.Notifications,"Notifications",{}),
        Setting(icon = Icons.Default.QuestionMark,"FAQs",{}),
        Setting(icon = Icons.AutoMirrored.Filled.ContactSupport,"Contact Us",{}),
        Setting(icon = Icons.AutoMirrored.Filled.Logout,"Log Out",{
            dialogBoxShow = true
        }),
    )

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

            if(dialogBoxShow){
                Dialog(
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    ),
                    onDismissRequest = {dialogBoxShow = false},
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                    ){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Are You Sure!",
                                fontFamily = Constants.POOPINS_FONT_BOLD
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){

                                TextButton(
                                    onClick = {
                                        loginViewModel.logout(navHostController = navHostController)
                                    }
                                ) {
                                    Text("I'm Sure",
                                        fontFamily = Constants.POOPINS_FONT_BOLD,
                                        color = colorResource(Constants.BLUE_COLOR)
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        dialogBoxShow = false
                                    }
                                ) {
                                    Text("Cancel",
                                        fontFamily = Constants.POOPINS_FONT_BOLD,
                                        color = colorResource(Constants.GREEN_COLOR)
                                    )
                                }

                            }

                        }

                    }

                }

            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = userData.image,
                            placeholder = painterResource(R.drawable.puff_image),
                            contentDescription = "student_profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                        ){
                            Text(
                                "$name",
                                fontFamily = Constants.POOPINS_FONT_BOLD,
                                fontSize = 24.sp,
                                softWrap = true,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = colorResource(Constants.TEXT_COLOR)
                            )

                            Text(
                                "ID: $email",
                                fontFamily = Constants.POOPINS_FONT_BOLD,
                                fontSize = 14.sp,
                                softWrap = true,
                                overflow = TextOverflow.Ellipsis,
                                color = colorResource(Constants.TEXT_COLOR)
                            )


                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .weight(0.07f)
                                .clip(RoundedCornerShape(7.dp))
                                .background(colorResource(Constants.GREY)),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "notification_icon",
                                tint = colorResource(Constants.BACKGROUND_COLOR)
                            )

                        }

                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 7.dp
                        )
                    ) {}
                }

                item {

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 12.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(Constants.BACKGROUND_COLOR))
                        ) {

                            Text(
                                text = "Settings",
                                fontFamily = Constants.POOPINS_FONT_BOLD,
                                color = colorResource(Constants.TEXT_COLOR),
                                fontSize = 18.sp,
                                modifier = Modifier.padding(12.dp)
                            )

                            for(setting in settings){
                                SettingItem(setting = setting)
                            }


                        }
                    }

                }


            }
        }

    }


}

data class Setting(
    val icon:ImageVector,
    val name:String,
    val onClick:()->Unit
)

@Composable
fun SettingItem(modifier: Modifier = Modifier,setting: Setting) {

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Black)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(colorResource(Constants.BACKGROUND_COLOR))
            .padding(4.dp)
            .clickable { setting.onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = setting.icon,
            contentDescription = "profile_icon",
            modifier = Modifier.weight(0.2f),
            tint = colorResource(Constants.TEXT_COLOR)
        )

        Text(
            text = setting.name,
            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
            modifier = Modifier.weight(0.6f),
            color = colorResource(Constants.TEXT_COLOR)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "profile_icon",
            modifier = Modifier.weight(0.2f),
            tint = colorResource(Constants.TEXT_COLOR)
        )

    }

}
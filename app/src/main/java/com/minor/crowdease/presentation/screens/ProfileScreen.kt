package com.minor.crowdease.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.minor.crowdease.R
import com.minor.crowdease.utlis.Constants


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {

    ProfileScreenPreview()

}

@Preview
@Composable
private fun ProfileScreenPreview() {

    Scaffold(modifier = Modifier.fillMaxSize()) {

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

                    Image(
                        painter = painterResource(R.drawable.puff_image),
                        contentDescription = "student_profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            "Vandan Nandwana",
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                            fontSize = 24.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )

                        Text(
                            "ID: 2203051050XXX",
                            fontFamily = Constants.POOPINS_FONT_BOLD,
                            fontSize = 14.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )


                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
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

data class Setting(
    val icon:ImageVector,
    val name:String
)

val settings = listOf(
    Setting(icon = Icons.Default.Person,"Edit Profile"),
    Setting(icon = Icons.Default.Notifications,"Notifications"),
    Setting(icon = Icons.Default.DarkMode,"Dark Mode"),
    Setting(icon = Icons.Default.QuestionMark,"FAQs"),
    Setting(icon = Icons.AutoMirrored.Filled.ContactSupport,"Contact Us"),
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
            .padding(4.dp),
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
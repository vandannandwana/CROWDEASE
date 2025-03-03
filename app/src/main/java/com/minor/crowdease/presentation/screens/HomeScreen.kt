package com.minor.crowdease.presentation.screens
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.minor.crowdease.utlis.Constants

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    HomeScreenPreview()
}
@Composable
private fun HomeScreenPreview() {
    val firstProgress = 0.3f
    val secondProgress = 0.5f
    val thirdProgress = 0.7f

    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier) {
                        Text(
                            "Parul University",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            color = colorResource(Constants.BLUE_COLOR),
                            fontSize = 32.sp
                        )
                        Text(
                            "Food Court",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 18.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "notification_icon"
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
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.padding(12.dp)) {
                    Column {
                        Text(
                            "Hey There!",
                            fontFamily = Constants.POOPINS_FONT_SEMI_BOLD,
                            fontSize = 34.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )
                        Text(
                            "What would you like  to eat today?",
                            fontFamily = Constants.POOPINS_FONT_REGULAR,
                            fontSize = 18.sp,
                            color = colorResource(Constants.TEXT_COLOR)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(7.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(Constants.DARK_GREY)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 7.dp
                            )
                        ) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)) {
                                    Text(
                                        "Current Rush Status",
                                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                                        color = colorResource(Constants.TEXT_COLOR)
                                    )

                                    Text(
                                        "Capitol",
                                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                                        color = colorResource(Constants.GREEN_COLOR)
                                    )
                                    LinearProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .clip(RoundedCornerShape(7.dp)),
                                        progress = { firstProgress },
                                        color = Color(0xFF22C45E),
                                        trackColor = Color(0xFF22C45E).copy(alpha = 0.2f)
                                    )
                                    Text(
                                        "Medical",
                                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                                        color = Color(0xFFE8B208)
                                    )
                                    LinearProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .clip(RoundedCornerShape(7.dp)),
                                        progress = { secondProgress },
                                        color = Color(0xFFE8B208),
                                        trackColor = Color(0xFFE8B208).copy(alpha = 0.2f)
                                    )
                                    Text(
                                        "Greenzy",
                                        fontFamily = Constants.POOPINS_FONT_REGULAR,
                                        color = Color(0xFFED4444)
                                    )
                                    LinearProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)
                                            .clip(RoundedCornerShape(7.dp)),
                                        progress = { thirdProgress },
                                        color = Color(0xFFED4444),
                                        trackColor = Color(0xFFED4444).copy(alpha = 0.2f)
                                    )
                                }

                            }
                        }
                    }
                }


            }


        }

    }

}
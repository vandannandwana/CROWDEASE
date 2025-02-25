package com.minor.crowdease.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.indendshape.ShapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.minor.crowdease.utlis.Constants
import kotlinx.coroutines.launch

enum class NavigationBarItems(val icon: ImageVector) {

    Person(icon = Icons.Default.Home),
    Search(icon = Icons.Default.Search),
    Profile(icon = Icons.Default.Person)

}

@Composable
fun MainScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val navBarItems = remember { NavigationBarItems.entries.toTypedArray() }
    val pagerState = rememberPagerState { NavigationBarItems.entries.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentPage) {
        scope.launch {
            pagerState.animateScrollToPage(
                currentPage,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        HorizontalPager(pagerState, userScrollEnabled = false) { page->

            when(page){
                0 -> {
                    HomeScreen(navHostController = navHostController)
                }
                1->{
                    SearchScreen(navHostController = navHostController)
                }
                2->{
                    ProfileScreen(navHostController = navHostController)
                }
                else->{
                    HomeScreen(navHostController = navHostController)
                }
            }

        }

        BottomNavigationBar(modifier = Modifier.align(Alignment.BottomCenter), currentPage = currentPage, navBarItems = navBarItems) {
            currentPage = it
        }

    }

}

@Composable
fun BottomNavigationBar(
    modifier: Modifier,
    currentPage: Int,
    navBarItems: Array<NavigationBarItems>,
    onItemClick: (page: Int) -> Unit
) {

    AnimatedNavigationBar(
        selectedIndex = currentPage, modifier = modifier
            .padding(24.dp),
        ballColor = Constants.BLUE_COLOR,
        ballAnimation = com.exyte.animatednavbar.animation.balltrajectory.Straight(tween(700)),
        cornerRadius = ShapeCornerRadius(60f, 60f, 60f, 60f)
    ) {

        navBarItems.forEach { item ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .noRippleClickable { onItemClick(item.ordinal) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (currentPage == item.ordinal) Constants.BLUE_COLOR else Color.Gray
                )
            }

        }

    }

}
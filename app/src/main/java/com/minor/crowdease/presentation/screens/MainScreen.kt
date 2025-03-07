package com.minor.crowdease.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
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
fun MainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val navBarItems = NavigationBarItems.entries // No need for remember, it's static
    val pagerState = rememberPagerState(initialPage = 0) { navBarItems.size }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true // Consider enabling if swipe is desired
        ) { page ->
            when (navBarItems[page]) { // Use enum instead of indices
                NavigationBarItems.Person -> HomeScreen(navHostController = navHostController)
                NavigationBarItems.Search -> SearchScreen(navHostController = navHostController)
                NavigationBarItems.Profile -> ProfileScreen(navHostController = navHostController)
            }
        }

        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            currentPage = pagerState.currentPage,
            navBarItems = navBarItems.toTypedArray(),
            onItemClick = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index) // Smooth scrolling
                }
            }
        )
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    currentPage: Int,
    navBarItems: Array<NavigationBarItems>,
    onItemClick: (page: Int) -> Unit
) {
    AnimatedNavigationBar(
        selectedIndex = currentPage,
        modifier = modifier.padding(24.dp),
        ballColor = colorResource(Constants.BLUE_COLOR),
        ballAnimation = Straight(tween(400, easing = FastOutSlowInEasing)), // Faster animation
        cornerRadius = ShapeCornerRadius(60f, 60f, 60f, 60f)
    ) {
        navBarItems.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // Custom ripple if needed
                    ) { onItemClick(item.ordinal) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name, // Accessibility improvement
                    tint = if (currentPage == item.ordinal) {
                        colorResource(Constants.BLUE_COLOR)
                    } else {
                        colorResource(Constants.GREY)
                    }
                )
            }
        }
    }
}
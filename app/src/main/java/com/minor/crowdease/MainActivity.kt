package com.minor.crowdease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.minor.crowdease.navigations.Navigation
import com.minor.crowdease.presentation.screens.ProfileScreen
import com.minor.crowdease.ui.theme.CROWDEASETheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CROWDEASETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val ip = innerPadding
                    val navHostController = rememberNavController()
                    Navigation(navHostController = navHostController)
                }
            }
        }
    }
}
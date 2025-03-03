package com.minor.crowdease

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.minor.crowdease.data.dto.chat.ChatScreen
import com.minor.crowdease.data.dto.chat.ChatViewModel
import com.minor.crowdease.data.dto.chat.EnterTokenDialog
import com.minor.crowdease.navigations.Navigation
import com.minor.crowdease.presentation.screens.ProfileScreen
import com.minor.crowdease.ui.theme.CROWDEASETheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissons()
        enableEdgeToEdge()
        setContent {
            CROWDEASETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val ip = innerPadding
//                    val navHostController = rememberNavController()
//                    Navigation(navHostController = navHostController)

                    val viewModel = hiltViewModel<ChatViewModel>()

                    val state = viewModel.state.collectAsStateWithLifecycle().value

                    if(state.isEnteringToken){

                        EnterTokenDialog(
                            token = state.remoteToken,
                            onTokenChange = viewModel::onRemoteTokenChange,
                            onSubmit = viewModel::onSubmitRemoteToken
                        )

                    }else{
                        ChatScreen(
                            messageText = state.messageText,
                            onMessageSend = {viewModel.sendMessage(isBroadCast = false)},
                            onMessageBroadcast = {viewModel.sendMessage(isBroadCast = true)},
                            onMessageChange = viewModel::onMessageChange
                        )
                    }


                }
            }
        }
    }

    private fun requestNotificationPermissons() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU){

            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )== PackageManager.PERMISSION_GRANTED

            if(!hasPermission){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }

        }
    }
}
package com.minor.crowdease.presentation.screens
import android.content.ContentValues.TAG
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.firebase.messaging.FirebaseMessaging
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.tasks.OnCompleteListener
import com.minor.crowdease.R

@Composable
fun MessageScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Button(onClick = {
            getFCMToken()
        }) {
            Text(text = "Get FCM Token")
        }

    }



}

fun getFCMToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result
        Log.d(TAG, token)
        Log.d("VANDANJI",token)
    })
}

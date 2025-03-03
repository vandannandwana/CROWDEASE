package com.minor.crowdease.data.dto.chat

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun EnterTokenDialog(
    token:String,
    onTokenChange:(String)->Unit,
    onSubmit:()->Unit
){

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = { onSubmit() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            Column(
                modifier= Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .padding(16.dp)
            ){

                OutlinedTextField(
                    value = token,
                    onValueChange = onTokenChange,
                    modifier =  Modifier.fillMaxWidth(),
                    placeholder = {
                        androidx.compose.material3.Text(text = "Enter Token")
                    },
                    maxLines = 1
                )

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                val localToken = Firebase.messaging.token.await()
                                clipboardManager.setText(AnnotatedString(localToken))
                                 Toast.makeText(context,"Copied Local token",Toast.LENGTH_LONG).show()

                            }
                        }
                    ) {
                        androidx.compose.material3.Text(text = "Copy Token")
                    }

                    Button(
                        onClick = onSubmit
                    ) {
                        Text("Submit")
                    }

                }

            }
        }
    )



}
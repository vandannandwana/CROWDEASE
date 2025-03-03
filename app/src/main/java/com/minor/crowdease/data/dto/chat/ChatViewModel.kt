package com.minor.crowdease.data.dto.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val api:FCMApi): ViewModel() {

        var state = MutableStateFlow(ChatState())
            private set

    fun onRemoteTokenChange(newToken:String){

        state.value = state.value.copy(
            remoteToken = newToken
        )

    }

    fun onSubmitRemoteToken(){
        state.value = state.value.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChange(message:String){
        state.value = state.value.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadCast:Boolean){


        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if(isBroadCast) null else state.value.remoteToken,
                notification = NotificationBody(
                    title = "New Message",
                    body = state.value.messageText
                )
            )

            try {
                if (isBroadCast) {

                    api.broadcast(messageDto)

                } else {
                    api.sendMessage(messageDto)
                }
                state.value = state.value.copy(
                    messageText = ""
                )
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

    }

}
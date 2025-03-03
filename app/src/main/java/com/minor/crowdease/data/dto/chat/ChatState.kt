package com.minor.crowdease.data.dto.chat

data class ChatState(
    val isEnteringToken:Boolean = false,
    val remoteToken:String = "",
    val messageText:String = ""
)
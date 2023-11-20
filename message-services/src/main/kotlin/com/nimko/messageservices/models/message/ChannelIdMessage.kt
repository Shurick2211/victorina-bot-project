package com.nimko.messageservices.models.message

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User

data class ChannelIdMessage (
    val adminId:String,
    val channelId:String,
    val channel:Chat,
    val user: User
)
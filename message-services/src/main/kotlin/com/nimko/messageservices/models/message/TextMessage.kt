package com.nimko.messageservices.models.message

import org.telegram.telegrambots.meta.api.objects.User

data class TextMessage(
    val userId:String,
    val textMessage:String,
    val user:User?
)

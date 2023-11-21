package com.nimko.messageservices.telegram.models.message

import org.telegram.telegrambots.meta.api.objects.CallbackQuery

data class ResponseDataMessage(
    val chatId:String,
    val callbackQuery: CallbackQuery
)

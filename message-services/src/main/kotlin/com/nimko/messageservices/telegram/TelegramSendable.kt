package com.nimko.messageservices.telegram

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup

interface TelegramSendable {

    fun sendMessage(sendMessage: SendMessage)

    fun sendAnswerInline(answer: EditMessageReplyMarkup)


}


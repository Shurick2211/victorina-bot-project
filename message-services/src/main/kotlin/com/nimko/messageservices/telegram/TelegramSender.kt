package com.nimko.messageservices.telegram

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup

abstract class TelegramSender(val bot: TelegramBot) {

    abstract fun sendMessage(sendMessage: SendMessage)

    abstract fun sendAnswerInline(answer: EditMessageReplyMarkup)


}


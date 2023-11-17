package com.nimko.messageservices.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class TelegramBot (@Value("\${token}")botToken: String?) : TelegramLongPollingBot(botToken) {
    override fun getBotUsername(): String {
        return "Frst_ex_bot"
    }

    override fun onUpdateReceived(update: Update?) {
        println(update)
    }

}
package com.nimko.messageservices.telegram

import org.telegram.telegrambots.meta.api.objects.Update

interface TelegramListener {
    fun getUpdate(update: Update)
    fun getBot(bot :TelegramBot)
}
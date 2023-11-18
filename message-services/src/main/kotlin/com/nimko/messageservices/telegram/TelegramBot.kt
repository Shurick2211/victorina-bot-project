package com.nimko.messageservices.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class TelegramBot ( @Value("\${token.telega}") botToken:String): TelegramLongPollingBot(botToken) {

     val log = LoggerFactory.getLogger("Telega")

    @Value("\${name.telega}")
    lateinit var name:String;
    override fun getBotUsername(): String = name
    override fun onUpdateReceived(update: Update?) {
        log.info(update.toString())
      
    }


}
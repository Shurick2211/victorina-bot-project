package com.nimko.messageservices.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class TelegramBot (
    @Value("\${token.telega}") botToken:String,
    @Autowired val listener: TelegramListener

)
    : TelegramLongPollingBot(botToken){

     val log = LoggerFactory.getLogger("Telega")

    @Value("\${name.telega}")
    lateinit var name:String;

    init{
        listener.getBot(this)
    }
    override fun onUpdateReceived(update: Update?) {
        log.info(update.toString())
        listener.getUpdate(update!!)
    }

    override fun getBotUsername(): String = name

}
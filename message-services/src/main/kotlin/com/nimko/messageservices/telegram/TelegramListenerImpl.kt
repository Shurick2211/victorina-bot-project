package com.nimko.messageservices.telegram

import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class TelegramListenerImpl(
    @Autowired val messageListener: MessageServicesListener,
): TelegramListener {



    override fun getUpdate(update: Update) {
        when{
            update.hasMessage() -> {
                messageListener.getTextMessage(TextMessage(update.message.chatId.toString(), update.message.text))
            }
            update.hasCallbackQuery() -> {
                println(update.callbackQuery.message)
            }

        }
    }

    override fun getBot(bot: TelegramBot) {
        val sender = MessageServicesSenderImpl(bot)
        messageListener.getSender(sender)
    }


}
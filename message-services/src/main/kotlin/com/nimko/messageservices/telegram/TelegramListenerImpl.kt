package com.nimko.messageservices.telegram

import com.nimko.messageservices.models.message.ChannelIdMessage
import com.nimko.messageservices.models.message.PollMessage
import com.nimko.messageservices.models.message.ResponseDataMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.services.MessageServicesListener
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
                val chatId = update.message.chatId.toString()
                val text = update.message.text
                val user = update.message.from
                if (update.message.forwardFromChat == null) {
                    if (text.startsWith("/start"))
                        messageListener.getTextMessage(TextMessage(chatId, "Hello!", user))
                    else
                        messageListener.getPoll(PollMessage(chatId,"Are you stupid", listOf("yes", "no", "maybe", "a little"), 2))
                }
                else {
                    messageListener.getChannelId(ChannelIdMessage(chatId,
                        update.message.forwardFromChat.id.toString(),
                        update.message.forwardFromChat, user))
                }
            }
            update.hasCallbackQuery() -> {
                messageListener.getDataMessage(
                    ResponseDataMessage(update.callbackQuery.from.id.toString(),update.callbackQuery))
            }

        }
    }

    override fun getBot(bot: TelegramBot) {
        val sender = MessageServicesSenderImpl(bot)
        messageListener.getSender(sender)
    }


}
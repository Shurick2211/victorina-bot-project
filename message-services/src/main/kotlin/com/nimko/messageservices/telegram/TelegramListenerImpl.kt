package com.nimko.messageservices.telegram

import com.nimko.messageservices.models.message.*
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
                   messageListener.getTextMessage(TextMessage(chatId,text,user))
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
            update.hasPollAnswer() -> {
                messageListener.getPollAnswer(
                    PollAnswer(update.pollAnswer.user.id.toString(), update.pollAnswer.optionIds[0])
                )
            }

        }
    }

    override fun getBot(bot: TelegramBot) {
        val sender = MessageServicesSenderImpl(bot)
        messageListener.getSender(sender)
    }


}
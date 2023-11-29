package com.nimko.messageservices.telegram

import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.ResponseDataMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
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
                   messageListener.onTextMessage(TextMessage(chatId,text,user))
                } else {
                    messageListener.onChannelId(
                        ChannelIdMessage(chatId,
                        update.message.forwardFromChat.id.toString(),
                        update.message.forwardFromChat, user)
                    )
                }
            }
            update.hasCallbackQuery() -> {
                messageListener.onDataMessage(
                    ResponseDataMessage(update.callbackQuery.from.id.toString(),update.callbackQuery)
                )
            }
            update.hasPollAnswer() -> {
                messageListener.onPollAnswer(
                    PollAnswer(update.pollAnswer.user.id.toString(), update.pollAnswer.pollId, update.pollAnswer.user.languageCode ,update.pollAnswer.optionIds)
                )
            }

        }
    }

    override fun getBot(bot: TelegramBot) {
        val sender = MessageServicesSenderImpl(bot)
        messageListener.getSender(sender)
    }


}
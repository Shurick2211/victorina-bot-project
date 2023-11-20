package com.nimko.bot.services

import com.nimko.messageservices.models.message.*
import com.nimko.messageservices.models.others.InlineButton
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User

@Service
class MessageServicesListenerImpl:MessageServicesListener {

    lateinit var sender: MessageServicesSender

    override fun getTextMessage(textMessage: TextMessage) {
        if (textMessage.textMessage.equals("Hello!"))
            sender.sendMenu(MenuMessage(textMessage.userId, "You say hello! ${textMessage.user}",listOf("For admin")))
        else
            sender.sendText(textMessage)
    }

    override fun getDataMessage(responseDataMessage: ResponseDataMessage) {
        sender.sendChangeInlineButton(ChangeInlineMessage(responseDataMessage.chatId,
            responseDataMessage.callbackQuery.message.messageId.toString()
            ,listOf(InlineButton("OK!", "ok"))))
    }

    override fun getChannelId(channelIdMessage: ChannelIdMessage) {
        println(channelIdMessage.channelId)
        sender.sendTextAndInlineButton(
            TextMessage(channelIdMessage.adminId,"${channelIdMessage.channel}", channelIdMessage.user),
            listOf(InlineButton("Yes!", "yes"), InlineButton("No!", "no")))
    }

    override fun getSender(sender: MessageServicesSender) {
        this.sender = sender
    }

    override fun getPoll(pollMessage: PollMessage) {
        sender.sendOnePoll(pollMessage)
    }
}
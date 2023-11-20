package com.nimko.bot.services

import com.nimko.messageservices.models.message.ChannelIdMessage
import com.nimko.messageservices.models.message.ResponseDataMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageServicesListenerImpl:MessageServicesListener {

    lateinit var sender: MessageServicesSender

    override fun getTextMessage(textMessage: TextMessage) {
       println(textMessage)
        sender.sendText(textMessage)
    }

    override fun getDataMessage(responseDataMessage: ResponseDataMessage) {
        println(responseDataMessage.data)
    }

    override fun getChannelId(channelIdMessage: ChannelIdMessage) {
        println(channelIdMessage.channelId)
    }

    override fun getSender(sender: MessageServicesSender) {
        this.sender = sender
    }
}
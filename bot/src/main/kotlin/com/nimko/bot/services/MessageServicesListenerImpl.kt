package com.nimko.bot.services

import com.nimko.messageservices.models.message.ChannelIdMessage
import com.nimko.messageservices.models.message.ResponseDataMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.services.MessageServicesListener
import org.springframework.stereotype.Service

@Service
class MessageServicesListenerImpl:MessageServicesListener {
    override fun getTextMessage(textMessage: TextMessage) {
        TODO("Not yet implemented")
    }

    override fun getDataMessage(responseDataMessage: ResponseDataMessage) {
        TODO("Not yet implemented")
    }

    override fun getChannelId(channelIdMessage: ChannelIdMessage) {
        TODO("Not yet implemented")
    }
}
package com.nimko.messageservices.services

import com.nimko.messageservices.models.message.ChannelIdMessage
import com.nimko.messageservices.models.message.ResponseDataMessage
import com.nimko.messageservices.models.message.TextMessage

interface MessageServicesListener {

    fun getTextMessage(textMessage : TextMessage)

    fun getDataMessage(responseDataMessage: ResponseDataMessage)

    fun getChannelId(channelIdMessage: ChannelIdMessage)

    fun getSender(sender: MessageServicesSender)
}
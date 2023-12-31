package com.nimko.messageservices.services

import com.nimko.messageservices.telegram.models.message.*

interface MessageServicesListener {

    fun onTextMessage(textMessage : TextMessage)

    fun onDataMessage(responseDataMessage: ResponseDataMessage)

    fun onChannelId(channelIdMessage: ChannelIdMessage)

    fun addSender(sender: MessageServicesSender)

    fun onSender():MessageServicesSender

    fun onPollAnswer(pollAnswer: PollAnswer)
}
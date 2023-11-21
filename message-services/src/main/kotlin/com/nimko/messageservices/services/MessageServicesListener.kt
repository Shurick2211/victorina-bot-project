package com.nimko.messageservices.services

import com.nimko.messageservices.telegram.models.message.*

interface MessageServicesListener {

    fun getTextMessage(textMessage : TextMessage)

    fun getDataMessage(responseDataMessage: ResponseDataMessage)

    fun getChannelId(channelIdMessage: ChannelIdMessage)

    fun getSender(sender: MessageServicesSender)

    fun getPoll(pollMessage: PollMessage)

    fun getPollAnswer(pollAnswer: PollAnswer)
}
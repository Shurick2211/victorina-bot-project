package com.nimko.bot.services

import com.nimko.bot.utils.PersonUtils
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.ResponseDataMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import org.telegram.telegrambots.meta.api.objects.User

interface PersonServices {

    fun registration(user:User, sender: MessageServicesSender)

    fun registrationCreator(user: User?,
                            responseDataMessage: ResponseDataMessage?,
                            channelIdMessage: ChannelIdMessage?,
                            sender: MessageServicesSender)

    fun onQuiz(user: User?,
               responseDataMessage: ResponseDataMessage?,
               pollAnswer: PollAnswer?,
               sender: MessageServicesSender)

    fun forFree(textMessage: TextMessage, sender: MessageServicesSender)

    fun getUtils():PersonUtils


}
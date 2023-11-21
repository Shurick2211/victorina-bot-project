package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.ResponseDataMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import com.nimko.messageservices.services.MessageServicesSender
import org.telegram.telegrambots.meta.api.objects.User

interface PersonServices {

    fun registration(user:User, sender: MessageServicesSender)

    fun registrationCreator(person: Person?,
                            responseDataMessage: ResponseDataMessage?,
                            channelIdMessage: ChannelIdMessage?,
                            sender: MessageServicesSender)

    fun onQuiz(person: Person?,
               responseDataMessage: ResponseDataMessage?,
               pollAnswer: PollAnswer?,
               sender: MessageServicesSender)

    fun forFree(textMessage: TextMessage, sender: MessageServicesSender)

    fun getPerson(userId:String): Person?
}
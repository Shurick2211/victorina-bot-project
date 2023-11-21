package com.nimko.bot.services

import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.utils.Commands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageServicesListenerImpl @Autowired constructor(
    val personServices: PersonServices
):MessageServicesListener {

    lateinit var sender: MessageServicesSender

    val log = LoggerFactory.getLogger("MSG_SERV")

    override fun getTextMessage(textMessage: TextMessage) {
        when(textMessage.textMessage){
            Commands.START.getCommand() -> {
                personServices.registration(textMessage.user!!,sender)
            }
            Commands.CREATOR.getCommand() -> {
                personServices.registrationCreator(
                    personServices.getPerson(textMessage.userId),
                    null,null, sender
                )
            }
            else -> {
                log.info(textMessage.toString())
            }
        }
    }

    override fun getDataMessage(responseDataMessage: ResponseDataMessage) {
        val person = personServices.getPerson(responseDataMessage.chatId)!!

        if (person.state == PersonState.REGISTRATION_CREATOR)
            personServices.registrationCreator(
                person = person,responseDataMessage,null,sender)
        else
            personServices.onQuiz(person,responseDataMessage,null, sender)

    }

    override fun getChannelId(channelIdMessage: ChannelIdMessage) {
        personServices.registrationCreator(null,null
            ,channelIdMessage,sender)
    }

    override fun getSender(sender: MessageServicesSender) {
        this.sender = sender
    }

    override fun getPoll(pollMessage: PollMessage) {
        sender.sendOnePoll(pollMessage)
    }

    override fun getPollAnswer(pollAnswer: PollAnswer) {
        personServices.onQuiz(null,null,pollAnswer,sender)
    }
}
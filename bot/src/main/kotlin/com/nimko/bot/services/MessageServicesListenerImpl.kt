package com.nimko.bot.services

import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.ResponseDataMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import com.nimko.messageservices.telegram.utils.Commands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageServicesListenerImpl @Autowired constructor(
    val personServices: PersonServices,
    val messageSource: ResourceBundleMessageSource
):MessageServicesListener {

    lateinit var sender: MessageServicesSender

    val log = LoggerFactory.getLogger("MSG_SERV")

    override fun onTextMessage(textMessage: TextMessage) {
        when(textMessage.textMessage){
            Commands.START.getCommand() -> {
                personServices.registration(textMessage.user!!,sender)
            }
            messageSource.getMessage("button.for.creator",null,
                Locale.forLanguageTag(textMessage.user!!.languageCode)) -> {
                    personServices.registrationCreator(
                        textMessage.user,null,null, sender
                    )
                }
            messageSource.getMessage("button.free.message",null,
                Locale.forLanguageTag(textMessage.user!!.languageCode)) -> {
                    personServices.forFree(textMessage, sender)
                }
            else -> {
                log.info(textMessage.toString())
            }
        }
    }

    override fun onDataMessage(responseDataMessage: ResponseDataMessage) {
        val person = personServices.getUtils().getPerson(responseDataMessage.callbackQuery.from.id.toString())!!

        if (person.state == PersonState.REGISTRATION_CREATOR)
            personServices.registrationCreator(
                responseDataMessage.callbackQuery.from,responseDataMessage,null,sender)
        else
            personServices.onQuiz(responseDataMessage.callbackQuery.from,
                responseDataMessage,null, sender)

    }

    override fun onChannelId(channelIdMessage: ChannelIdMessage) {
        personServices.registrationCreator(null,null
            ,channelIdMessage,sender)
    }

    override fun getSender(sender: MessageServicesSender) {
        this.sender = sender
    }


    override fun onPollAnswer(pollAnswer: PollAnswer) {
        personServices.onQuiz(null,null,pollAnswer,sender)
    }
}
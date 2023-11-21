package com.nimko.bot.services

import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.models.message.*
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageServicesListenerImpl:MessageServicesListener {

    @Autowired
    lateinit var personServices: PersonServices

    lateinit var sender: MessageServicesSender

    override fun getTextMessage(textMessage: TextMessage) {
        when(textMessage.textMessage){
            "/start" -> {
                personServices.registration(textMessage.user!!,sender)
            }
            else -> {

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

//        sender.sendChangeInlineButton(ChangeInlineMessage(responseDataMessage.chatId,
//            responseDataMessage.callbackQuery.message.messageId.toString()
//            ,listOf(InlineButton("OK!", "ok"))))
    }

    override fun getChannelId(channelIdMessage: ChannelIdMessage) {
        personServices.registrationCreator(null,null
            ,channelIdMessage,sender)

//        sender.sendTextAndInlineButton(
//            TextMessage(channelIdMessage.adminId,"${channelIdMessage.channel}", channelIdMessage.user),
//            listOf(InlineButton("Yes!", "yes"), InlineButton("No!", "no")))
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
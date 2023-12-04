package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.ResponseDataMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import com.nimko.messageservices.telegram.utils.CallbackData
import com.nimko.messageservices.telegram.utils.Commands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Service
class MessageServicesListenerImpl @Autowired constructor(
    val personServices: PersonServices,
    val messageSource: ResourceBundleMessageSource
):MessageServicesListener, PrizeServices {

    lateinit var sender: MessageServicesSender

    val log = LoggerFactory.getLogger("MSG_SERV")

    override fun onTextMessage(textMessage: TextMessage) {
        when{
            textMessage.textMessage.startsWith(Commands.START.getCommand()) -> {
                personServices.registration(textMessage.user!!,sender)
            }
            textMessage.textMessage == messageSource.getMessage("button.for.creator",null,
                Locale.forLanguageTag(textMessage.user!!.languageCode)) -> {
                    personServices.registrationCreator(
                        textMessage.user,null,null, sender
                    )
                }
            textMessage.textMessage == messageSource.getMessage("button.free.message",null,
                Locale.forLanguageTag(textMessage.user!!.languageCode)) -> {
                    personServices.forFree(textMessage, sender)
                }
            else -> {
                val person = personServices.getUtils().getPerson(textMessage.userId)!!
                if (person.state == PersonState.PRIZE){
                    person.state = PersonState.FREE
                    personServices.getUtils().savePerson(person)
                    personServices.getUtils().sendDeliveryAddress(textMessage,sender)
                } else log.info(textMessage.toString())
            }
        }
    }

    override fun onDataMessage(responseDataMessage: ResponseDataMessage) {
        val person = personServices.getUtils().getPerson(responseDataMessage.callbackQuery.from.id.toString())!!
        when{
            person.state == PersonState.REGISTRATION_CREATOR -> personServices.registrationCreator(
                responseDataMessage.callbackQuery.from,responseDataMessage,null,sender)

            responseDataMessage.callbackQuery.data.startsWith(CallbackData.PRIZE.toString()) -> {
                person.state = PersonState.PRIZE
                personServices.getUtils().deleteInlineKeyboard(responseDataMessage.chatId,
                    responseDataMessage.callbackQuery.message.messageId.toString(), sender)
                responseDataMessage.callbackQuery.data
                personServices.getUtils().savePerson(person)
            }

            else ->  personServices.onQuiz(responseDataMessage.callbackQuery.from,
                responseDataMessage,null, sender)
        }
    }

    override fun onChannelId(channelIdMessage: ChannelIdMessage) {
        personServices.registrationCreator(null,null
            ,channelIdMessage,sender)
    }

    override fun addSender(sender: MessageServicesSender) {
        this.sender = sender
    }


    override fun onPollAnswer(pollAnswer: PollAnswer) {
        personServices.onQuiz(null,null,pollAnswer,sender)
    }

    override fun playPrize(victorinas: List<VictorinaDto>) {
        victorinas.forEach {
            if (it.rightsAnsweredUserId != null) {
                val winnerNum = getWinnerNum(it.rightsAnsweredUserId!!)
                val winner = personServices.getUtils().getPerson(it.rightsAnsweredUserId!![winnerNum])!!
                personServices.getUtils().sendVictorinaWinnerMessage(winner, it, sender)
            }
        }
    }

    private fun getWinnerNum(userIds:List<String>):Int{
        return Random.nextInt(userIds.size)
    }

}
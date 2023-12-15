package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.utils.PersonRole
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
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class MessageServicesListenerImpl @Autowired constructor(
    val personServices: PersonServices
):MessageServicesListener, PrizeServices {

    lateinit var sender: MessageServicesSender

    val log = LoggerFactory.getLogger("MSG_SERV")

    override fun onTextMessage(textMessage: TextMessage) {
        when{
            textMessage.textMessage.startsWith(Commands.START.getCommand()) -> {
                val command = textMessage.textMessage.substring(Commands.START.name.length + 1).trim()
                if(command.length > 10) {
                    log.info(textMessage.textMessage + " : " + command + " : " + textMessage.userId)
                    personServices.startVictorinaToInvite(textMessage.user!!, command, sender)
                }
                else personServices.registration(textMessage.user!!,sender)
            }
            textMessage.textMessage.startsWith(Commands.CREATOR.getCommand()) -> {
                    personServices.registrationCreator(
                        textMessage.user,null,null, sender)
                }
            textMessage.textMessage.startsWith(Commands.REFRESH.getCommand()) -> {
                    personServices.forFree(textMessage, sender)
                }
            else -> {
                val person = personServices.getUtils().getPerson(textMessage.userId)!!
                if (person.state == PersonState.PRIZE){
                    person.state = PersonState.FREE
                    personServices.getUtils().savePerson(person)
                    personServices.getUtils().sendDeliveryAddress(textMessage,sender)
                } else {
                    log.info(textMessage.toString())
                    val isAdminMess = personServices.getUtils().getPerson(textMessage.userId)!!.role == PersonRole.ADMIN
                    if (isAdminMess)  sender.sendText(
                        TextMessage(textMessage.textMessage.split("#")[0], textMessage.textMessage.split("#")[1] ,null))
                }
            }
        }
    }

    override fun onDataMessage(responseDataMessage: ResponseDataMessage) {
        val person = personServices.getUtils().getPerson(responseDataMessage.callbackQuery.from.id.toString())
        when{
            person?.state == PersonState.REGISTRATION_CREATOR -> personServices.registrationCreator(
                responseDataMessage.callbackQuery.from,responseDataMessage,null, sender)

            responseDataMessage.callbackQuery.data.startsWith(CallbackData.PRIZE.toString()) -> {
                person!!.state = PersonState.PRIZE
                personServices.getUtils().deleteInlineKeyboard(responseDataMessage.chatId,
                    responseDataMessage.callbackQuery.message.messageId.toString(), sender)
                responseDataMessage.callbackQuery.data
                personServices.getUtils().savePerson(person!!)
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

    override fun onSender() = sender



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

    override fun startChannelVictorinasForPlayPrize(victorinas: List<VictorinaDto>) {
        victorinas.forEach{
            personServices.getUtils().sendChannelMessageForStartVictorina(it, sender)
        }
    }

    private fun getWinnerNum(userIds:List<String>):Int{
        return Random.nextInt(userIds.size)
    }

}
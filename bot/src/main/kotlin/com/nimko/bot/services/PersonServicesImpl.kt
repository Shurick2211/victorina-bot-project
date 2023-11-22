package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.telegram.utils.Commands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

@Component
class PersonServicesImpl @Autowired constructor(
    val personRepo:PersonRepo,
    val channelServices: ChannelServices):PersonServices{

    private final val START_MESSAGE = "Hello in Quiz-bot! "
    private final val START_REG_MES = "If you are a channel's admin - resend here a channel's post. \n " +
            "Else - push \"Ready\""
    private final val CANNEL_REG_MES = "If you want add others channels - resend here a channel's post. \n " +
            "Else - push \"Ready\""
    private final val FINISH_REGISTRATION_MESSAGE = "Registration you as creator quiz was end. Link in admin-site: " +
            "<a href=\"http://\">Admin-site</a>"

    override fun registration(user: User, sender: MessageServicesSender) {
        val person = getPerson(user.id.toString())
        if (person == null) {
            personRepo.save(
                Person(
                    user.id.toString(),
                    user.firstName, user.lastName, user.userName,
                    user.languageCode, null, null,
                    PersonState.FREE
                )
            )
        }
        sender.sendMenu(
            MenuMessage(user.id.toString(),
            START_MESSAGE+user.firstName, listOf(Commands.CREATOR.getCommand())
            )
        )
        //temporary
        sendFreeMessage(user.id.toString(), sender)

    }

    override fun registrationCreator(
        person: Person?,
        responseDataMessage: ResponseDataMessage?,
        channelIdMessage: ChannelIdMessage?,
        sender: MessageServicesSender
    ) {
        if(person != null && responseDataMessage == null && channelIdMessage == null){
            person.state = PersonState.REGISTRATION_CREATOR
            personRepo.save(person)
            sender.sendTextAndInlineButton(
                TextMessage(person.id, START_REG_MES, null),
                listOf(InlineButton("Ready!", "ready"))
            )
        }
        if(person == null && responseDataMessage == null && channelIdMessage != null){
            val admin = getPerson(channelIdMessage.adminId)!!
            if (admin.channelsIdAdmin == null){
                admin.channelsIdAdmin = ArrayList()
            }
            admin.channelsIdAdmin!!.add(channelIdMessage.channelId)
            channelServices.saveChannel(channelIdMessage.channel)
            sender.sendTextAndInlineButton(
                TextMessage(admin.id, CANNEL_REG_MES, null),
                listOf(InlineButton("Ready!", "ready"))
            )
        }
        if(person != null && responseDataMessage != null && channelIdMessage == null){
            person.state = PersonState.FREE
            personRepo.save(person)
            deleteInlineKeyboard(person.id,
                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
            sendRegistrationFinishMessage(person.id, sender)
            sendFreeMessage(person.id, sender)
        }
    }




    override fun onQuiz(person: Person?,
                        responseDataMessage: ResponseDataMessage?,
                        pollAnswer: PollAnswer?,
                        sender: MessageServicesSender
    ) {
        responseDataMessage?.let {
            if(responseDataMessage.callbackQuery.data == "start") {
                deleteInlineKeyboard(responseDataMessage.chatId,
                    responseDataMessage.callbackQuery.message.messageId.toString(), sender)
                //temporary
                sender.sendOnePoll(
                    PollMessage(
                        responseDataMessage.chatId,
                        "Are you stupid",
                        listOf("yes", "no", "maybe", "a little"),
                        2, "Hi-hi-hi!"
                    )
                )
            }
        }

    }

    override fun forFree(textMessage: TextMessage, sender: MessageServicesSender) {
        println(textMessage)
    }

    override fun getPerson(userId: String): Person? = personRepo.findById(userId).orElse(null)

    private fun deleteInlineKeyboard(chatId:String, messageId:String, sender: MessageServicesSender) {
        sender.sendChangeInlineButton(ChangeInlineMessage(chatId, messageId, emptyList()))
    }

    private fun sendFreeMessage(userId: String, sender: MessageServicesSender) {
        sender.sendTextAndInlineButton(
            TextMessage(userId,
                "Have victorina press start or no!", null),
            listOf(
                InlineButton("Start", "start"),
                InlineButton("No", "free")
            )
        )
    }

    private fun sendRegistrationFinishMessage(userId: String, sender: MessageServicesSender) {
        sender.sendText(TextMessage(userId, FINISH_REGISTRATION_MESSAGE, null))
    }

    private fun checkUserAsChannelMember(channelId: String, userId: String, sender: MessageServicesSender):Boolean? {
        return try{
            val status = sender.checkIsUserOfChannel(channelId, userId).status
            if(status == "kicked" || status == "left") false
            else true
        } catch (e:TelegramApiRequestException){
            null
        }
    }
}
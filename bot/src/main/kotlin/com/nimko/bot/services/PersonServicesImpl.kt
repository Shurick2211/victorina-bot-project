package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.util.*

@Component
class PersonServicesImpl @Autowired constructor(
    val personRepo:PersonRepo,
    val channelServices: ChannelServices,
    val messageSource: MessageSource
):PersonServices{

    override fun registration(user: User, sender: MessageServicesSender) {
        var person = getPerson(user.id.toString())
        if (person == null) {
            person = Person(
                user.id.toString(),
                user.firstName, user.lastName, user.userName,
                user.languageCode, null, null,
                PersonState.FREE
            )
            personRepo.save(
                person
            )
        }
        sender.sendMenu(
            MenuMessage(user.id.toString(),
            messageSource.getMessage("message.start",null,
                Locale.forLanguageTag(user.languageCode)) + user.firstName,
                listOf(messageSource.getMessage("button.for.creator",null,
                    Locale.forLanguageTag(user.languageCode))
                )
            )
        )
        //temporary
        sendFreeMessage(user.id.toString(), Locale.forLanguageTag(user.languageCode) ,sender)

    }

    override fun registrationCreator(
        user: User?,
        responseDataMessage: ResponseDataMessage?,
        channelIdMessage: ChannelIdMessage?,
        sender: MessageServicesSender
    ) {
        if(user != null && responseDataMessage == null && channelIdMessage == null){
            val person = personRepo.findById(user.id.toString()).get()
            person.state = PersonState.REGISTRATION_CREATOR
            personRepo.save(person)
            sender.sendTextAndInlineButton(
                TextMessage(person.id,
                    messageSource.getMessage("message.reg.start",
                        null,Locale.forLanguageTag(user.languageCode)),
                    null),
                listOf(InlineButton(
                    messageSource.getMessage("button.ready",null,
                        Locale.forLanguageTag(user.languageCode))
                    , "ready"))
            )
        }
        if(user == null && responseDataMessage == null && channelIdMessage != null){
            val admin = getPerson(channelIdMessage.adminId)!!
            if (admin.channelsIdAdmin == null){
                admin.channelsIdAdmin = ArrayList()
            }
            admin.channelsIdAdmin!!.add(channelIdMessage.channelId)
            channelServices.saveChannel(channelIdMessage.channel)
            //need save admina
            sender.sendTextAndInlineButton(
                TextMessage(admin.id,
                    messageSource.getMessage("message.reg.channel",
                        null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                    , null),
                listOf(InlineButton(messageSource.getMessage("button.ready",null,
                    Locale.forLanguageTag(channelIdMessage.user.languageCode))
                    , "ready"))
            )
        }
        if(user != null && responseDataMessage != null && channelIdMessage == null){
            val person = personRepo.findById(user.id.toString()).get()
            person.state = PersonState.FREE
            personRepo.save(person)
            deleteInlineKeyboard(user.id.toString(),
                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
            sendRegistrationFinishMessage(person.id,
                Locale.forLanguageTag(user.languageCode), sender)
            sendFreeMessage(user.id.toString(), Locale.forLanguageTag(user.languageCode) ,sender)
        }
    }




    override fun onQuiz(user: User?,
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

    override fun getPerson(userId:String): Person? = personRepo.findById(userId).orElse(null)

    private fun deleteInlineKeyboard(chatId:String, messageId:String, sender: MessageServicesSender) {
        sender.sendChangeInlineButton(ChangeInlineMessage(chatId, messageId, emptyList()))
    }

    private fun sendFreeMessage(userId: String, lang:Locale, sender: MessageServicesSender) {
        sender.sendTextAndInlineButton(
            TextMessage(userId,
                messageSource.getMessage("message.invite", null, lang),
                null),
            listOf(
                InlineButton( messageSource.getMessage("button.start",
                    null,lang), "start"),
                InlineButton(messageSource.getMessage("button.not.yet",
                    null,lang), "free")
            )
        )
    }

    private fun sendRegistrationFinishMessage(userId: String,lang:Locale, sender: MessageServicesSender) {
        sender.sendText(TextMessage(userId, messageSource.getMessage("message.reg.finish",null, lang), null))
    }

    private fun checkUserAsChannelMember(channelId: String, userId: String, sender: MessageServicesSender):Boolean? {
        return try{
            val status = sender.checkIsUserOfChannel(channelId, userId).status
            !(status == "kicked" || status == "left")
        } catch (e:TelegramApiRequestException){
            null
        }
    }
}
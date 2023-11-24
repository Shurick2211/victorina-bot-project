package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.repositories.PersonRepo
import com.nimko.messageservices.telegram.utils.CallbackData
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.util.*

@Component
class PersonServicesImpl @Autowired constructor(
    val personRepo:PersonRepo,
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
                    , CallbackData.READY))
            )
        }
        if(user == null && responseDataMessage == null && channelIdMessage != null){
            saveChannelForAdmin(channelIdMessage)

            sender.sendTextAndInlineButton(
                TextMessage(channelIdMessage.adminId,
                    messageSource.getMessage("message.reg.channel",
                        null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                    , null),
                listOf(InlineButton(messageSource.getMessage("button.ready",null,
                    Locale.forLanguageTag(channelIdMessage.user.languageCode))
                    , CallbackData.READY))
            )
        }
        if(user != null && responseDataMessage != null && channelIdMessage == null){
            val person = personRepo.findById(user.id.toString()).get()
            person.state = PersonState.FREE
            personRepo.save(person)
            deleteInlineKeyboard(user.id.toString(),
                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
            sendRegistrationFinishMessage(user.id.toString(),
                Locale.forLanguageTag(user.languageCode), sender)
            sendFreeMessage(user.id.toString(), Locale.forLanguageTag(user.languageCode) ,sender)
        }
    }

    private fun saveChannelForAdmin(channelIdMessage: ChannelIdMessage) {
        val admin = getPerson(channelIdMessage.adminId)!!
        if (admin.channelsAdmin == null){
            admin.channelsAdmin = ArrayList()
        }
        if (admin.channelsAdmin!!.indexOf(channelIdMessage) == -1){
            admin.channelsAdmin!!.add(channelIdMessage)
            personRepo.save(admin)
        }
    }


    override fun onQuiz(user: User?,
                        responseDataMessage: ResponseDataMessage?,
                        pollAnswer: PollAnswer?,
                        sender: MessageServicesSender
    ) {
        responseDataMessage?.let {
           if(pollAnswer != null){
               println(pollAnswer)
           }



            if(responseDataMessage.callbackQuery.data == CallbackData.START.toString()) {
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
                    null,lang), CallbackData.START),
                InlineButton(messageSource.getMessage("button.not.yet",
                    null,lang), CallbackData.FREE)
            )
        )
    }

    @Value("\${my.address}") private lateinit var url:String
    @Value("\${server.port}") private lateinit var port:String
    private fun sendRegistrationFinishMessage(userId: String,lang:Locale, sender: MessageServicesSender) {
        sender.sendTextAndInlineButton(TextMessage(userId,
            messageSource.getMessage("message.reg.finish",null, lang), null),
            listOf(InlineButton(messageSource.getMessage("button.link",null, lang),
                CallbackData.LINK, url="${url}:${port}?user=${userId}")
                )
        )
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
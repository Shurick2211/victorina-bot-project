package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.models.Quiz
import com.nimko.bot.models.Victorina
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.utils.PersonRole
import com.nimko.messageservices.telegram.utils.CallbackData
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.telegram.utils.PollType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.util.*
import javax.print.attribute.standard.JobOriginatingUserName
import kotlin.collections.ArrayList

@Component
class PersonServicesImpl @Autowired constructor(
    val personRepo:PersonRepo,
    val messageSource: MessageSource,
    val victorinaServices: VictorinaServices
):PersonServices{

    override fun registration(user: User, sender: MessageServicesSender) {
        var person = getPerson(user.id.toString())
        if (person == null) {
            person = Person(
                user.id.toString(),
                user.firstName, user.lastName, user.userName,
                user.languageCode, null, null,
                PersonState.FREE,
                PersonRole.USER,
                null
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

        sendFreeMessage(user.id.toString(), user.userName, Locale.forLanguageTag(user.languageCode) ,sender)
    }

    override fun registrationCreator(
        user: User?,
        responseDataMessage: ResponseDataMessage?,
        channelIdMessage: ChannelIdMessage?,
        sender: MessageServicesSender
    ) {
        //start registration
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
                    , CallbackData.READY.toString()))
            )
        }
        //add channel for creators
        if(user == null && responseDataMessage == null && channelIdMessage != null){
            val mess =
                if (checkUserAsChannelMember(channelIdMessage.channelId,channelIdMessage.adminId,sender) == null)
                    TextMessage(channelIdMessage.adminId,
                        messageSource.getMessage("message.no.admin.channel",
                            null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                        , null)
                else {
                    saveChannelForAdmin(channelIdMessage, sender)
                    TextMessage(channelIdMessage.adminId,
                        messageSource.getMessage("message.reg.channel",
                            null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                        , null)
                }

            sender.sendTextAndInlineButton(mess,
                listOf(InlineButton(messageSource.getMessage("button.ready",null,
                    Locale.forLanguageTag(channelIdMessage.user.languageCode))
                    , CallbackData.READY.toString()))
            )
        }
        //end of registration
        if(user != null && responseDataMessage != null && channelIdMessage == null){
            val person = personRepo.findById(user.id.toString()).get()
            person.state = PersonState.FREE
            if(person.role == PersonRole.USER) person.role = PersonRole.QUIZ_CREATOR
            personRepo.save(person)
            deleteInlineKeyboard(user.id.toString(),
                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
            sendRegistrationFinishMessage(user.id.toString(),
                Locale.forLanguageTag(user.languageCode), sender)
            sendFreeMessage(user.id.toString(), user.userName, Locale.forLanguageTag(user.languageCode) ,sender)
        }
    }

    private fun saveChannelForAdmin(channelIdMessage: ChannelIdMessage, sender: MessageServicesSender) {
        val admin = getPerson(channelIdMessage.adminId)!!
        if (admin.channelsAdmin == null){
            admin.channelsAdmin = ArrayList()
        }
        if (admin.channelsAdmin!!.indexOf(channelIdMessage) == -1){
            if(channelIdMessage.channel.inviteLink == null) {
                channelIdMessage.channel.inviteLink =
                sender.getInviteChannelLink(channelIdMessage.channelId).inviteLink
            }
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
            //on quiz
           if(pollAnswer != null){
               println(pollAnswer)
           }

            //start quiz
            if(responseDataMessage.callbackQuery.data != null) {
                when{
                    responseDataMessage.callbackQuery.data.startsWith(CallbackData.FREE.toString()) -> {
                        val userFree = responseDataMessage.callbackQuery.from
                        sendFreeMessage(userFree.id.toString(), userFree.userName,
                            Locale.forLanguageTag(userFree.languageCode), sender)
                    }
                    responseDataMessage.callbackQuery.data.startsWith(CallbackData.SUBSCRIBE.toString()) -> {
                        sender.sendChangeInlineButton(
                            ChangeInlineMessage(
                                responseDataMessage.callbackQuery.from.id.toString(),
                                responseDataMessage.callbackQuery.message.messageId.toString(),
                                listOf(InlineButton(messageSource.getMessage("button.ready",null,
                                    Locale.forLanguageTag(responseDataMessage.callbackQuery.from.languageCode)),
                                    "${CallbackData.QUIZ}#${responseDataMessage.callbackQuery.data.split("#")[1]}"),
                                    InlineButton(messageSource.getMessage("button.for.cancel", null ,
                                        Locale.forLanguageTag(responseDataMessage.callbackQuery.from.languageCode)),
                                        CallbackData.FREE.toString()
                                    )
                                )
                            ))
                    }
                    else -> {
                        val victorinaId =
                            if(responseDataMessage.callbackQuery.data.startsWith(CallbackData.QUIZ.toString())){
                                responseDataMessage.callbackQuery.data.split("#")[1]
                            }
                            else{
                                deleteInlineKeyboard(responseDataMessage.chatId,
                                    responseDataMessage.callbackQuery.message.messageId.toString(), sender)
                                responseDataMessage.callbackQuery.data
                            }
                        val victorina = victorinaServices.getVictorinaById(victorinaId)
                        val person = getPerson(responseDataMessage.chatId)!!
                        sendStartVictorinaMessage(person, victorina, sender)
                    }
                }
            }
        }
    }

    private fun sendStartVictorinaMessage(person: Person, victorina: Victorina, sender: MessageServicesSender) {
        val mess = TextMessage(person.id, victorina.title,null)
        val isChannelUser =
            if (victorina.channel != null) checkUserAsChannelMember(victorina.channel.channelId, person.id, sender)
            else true
        if (isChannelUser == true) {
            if (person.quizes == null) person.quizes = ArrayList()
            val quiz = Quiz(victorina.id!!, ArrayList())
            person.quizes!!.add(quiz)
            person.state = PersonState.IN_VICTORINA
            personRepo.save(person)
            sender.sendText(mess)
            val type = if (victorina.questions[0].rightAnswer.size > 1)
                PollType.REGULAR else PollType.QUIZ
            sender.sendOnePoll(
                PollMessage(
                person.id, victorina.questions[0].text, victorina.questions[0].answers,
                    victorina.questions[0].rightAnswer, null, type = type.getType()
            ))
        } else {
            sender.sendTextAndInlineButton(TextMessage(person.id,
                messageSource.getMessage("message.subscribe", null, Locale.forLanguageTag(person.languageCode))
                , null),
                listOf(InlineButton(victorina.channel!!.channelName,
                    "${CallbackData.SUBSCRIBE}#${victorina.id}", url = victorina.channel!!.url))
            )
        }

    }

    override fun forFree(textMessage: TextMessage, sender: MessageServicesSender) {
        println(textMessage)
    }

    override fun getPerson(userId:String): Person? = personRepo.findById(userId).orElse(null)

    private fun deleteInlineKeyboard(chatId:String, messageId:String, sender: MessageServicesSender) {
        sender.sendChangeInlineButton(ChangeInlineMessage(chatId, messageId, emptyList()))
    }

    private fun sendFreeMessage(userId: String, userName: String, lang:Locale, sender: MessageServicesSender) {
        val listButton = ArrayList<InlineButton>()
        victorinaServices.getActiveVictorin().forEach {
            var name = "${it.name} ${messageSource.getMessage("message.from",null, 
                lang)} - "
            name += if (it.channel !== null) it.channel.channelName
            else userName
            val button = InlineButton(name, it.id!!)
            listButton.add(button)
        }
        sender.sendTextAndInlineButton(
            TextMessage(userId,
                messageSource.getMessage("message.invite", null, lang),
                null),
            listButton, 1
        )
    }

    @Value("\${my.address}") private lateinit var url:String
    @Value("\${server.port}") private lateinit var port:String
    private fun sendRegistrationFinishMessage(userId: String,lang:Locale, sender: MessageServicesSender) {
        sender.sendTextAndInlineButton(TextMessage(userId,
            messageSource.getMessage("message.reg.finish",null, lang), null),
            listOf(InlineButton(messageSource.getMessage("button.link",null, lang),
                CallbackData.LINK.toString(), url="${url}:${port}?user=${userId}")
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
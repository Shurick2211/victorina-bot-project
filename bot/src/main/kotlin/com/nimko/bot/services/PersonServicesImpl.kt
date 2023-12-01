package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.utils.PersonRole
import com.nimko.bot.utils.PersonState
import com.nimko.bot.utils.PersonUtils
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.telegram.utils.CallbackData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import java.util.*

@Component
class PersonServicesImpl @Autowired constructor(
    private val messageSource: MessageSource,
    private val victorinaServices: VictorinaServices,
    private val personUtils: PersonUtils
):PersonServices{

    override fun registration(user: User, sender: MessageServicesSender) {
        var person = personUtils.getPerson(user.id.toString())
        if (person == null) {
            person = Person(
                user.id.toString(),
                user.firstName, user.lastName, user.userName,
                user.languageCode, null, null,
                PersonState.FREE,
                PersonRole.USER,
                null
            )
            personUtils.savePerson(person)
        }
        sender.sendMenu(
            MenuMessage(user.id.toString(),
            messageSource.getMessage("message.start",null,
                Locale.forLanguageTag(user.languageCode)) + user.firstName,
                listOf(messageSource.getMessage("button.for.creator",null,
                    Locale.forLanguageTag(user.languageCode)),
                    messageSource.getMessage("button.free.message",null,
                        Locale.forLanguageTag(user.languageCode))
                )
            )
        )

        personUtils.sendFreeMessage(user.id.toString(),  Locale.forLanguageTag(user.languageCode) ,sender)
    }

    override fun registrationCreator(
        user: User?,
        responseDataMessage: ResponseDataMessage?,
        channelIdMessage: ChannelIdMessage?,
        sender: MessageServicesSender
    ) {
        //start registration
        if(user != null && responseDataMessage == null && channelIdMessage == null){
            val person = personUtils.getPerson(user.id.toString())!!
            person.state = PersonState.REGISTRATION_CREATOR
            personUtils.savePerson(person)
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
                if (personUtils.checkUserAsChannelMember(channelIdMessage.channelId,channelIdMessage.adminId,sender) == null)
                    TextMessage(channelIdMessage.adminId,
                        messageSource.getMessage("message.no.admin.channel",
                            null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                        , null)
                else {
                    personUtils.saveChannelForAdmin(channelIdMessage, sender)
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
            val person = personUtils.getPerson(user.id.toString())!!
            person.state = PersonState.FREE
            if(person.role == PersonRole.USER) person.role = PersonRole.QUIZ_CREATOR
            personUtils.savePerson(person)
            personUtils.deleteInlineKeyboard(user.id.toString(),
                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
            personUtils.sendRegistrationFinishMessage(user.id.toString(),
                Locale.forLanguageTag(user.languageCode), sender)
        }
    }

    override fun onQuiz(user: User?,
                        responseDataMessage: ResponseDataMessage?,
                        pollAnswer: PollAnswer?,
                        sender: MessageServicesSender
    ) {
        //on quiz
        pollAnswer?.let{
            println(pollAnswer)
            val person = personUtils.getPerson(pollAnswer.userId)!!
            val currentQuiz = person.quizes!![person.quizes!!.size - 1]
            val victorina = victorinaServices.getVictorinaById(
                currentQuiz.victorinaId)
            currentQuiz.userAnswers.add(pollAnswer.answers)
            if(currentQuiz.userAnswers.size == victorina.questions.size) {
                person.state = PersonState.FREE
                person.quizes!![person.quizes!!.size - 1] = personUtils.checkVictorinaResult(currentQuiz,victorina)
                if (currentQuiz.isRightAnswered!!) victorinaServices.saveRightAnsweredUserId(person.id, victorina.id!!)
                personUtils.sendFinishQuizMessage(pollAnswer, victorina, currentQuiz, sender)
            } else {
                personUtils.sendQuestion(person,victorina,currentQuiz.userAnswers.size, sender)
            }
            personUtils.savePerson(person)
        }

        //start quiz
        responseDataMessage?.let {
            when{

                responseDataMessage.callbackQuery.data.startsWith(CallbackData.FREE.toString()) -> {
                    val userFree = responseDataMessage.callbackQuery.from
                    personUtils.sendFreeMessage(userFree.id.toString(),
                        Locale.forLanguageTag(userFree.languageCode), sender)
                }

                else -> {
                    val victorinaId =
                        if(responseDataMessage.callbackQuery.data.startsWith(CallbackData.QUIZ.toString())){
                            responseDataMessage.callbackQuery.data.split("#")[1]
                        }
                        else{
                            personUtils.deleteInlineKeyboard(responseDataMessage.chatId,
                                responseDataMessage.callbackQuery.message.messageId.toString(), sender)
                            responseDataMessage.callbackQuery.data
                        }
                    val victorina = victorinaServices.getVictorinaById(victorinaId)
                    val person = personUtils.getPerson(responseDataMessage.chatId)!!
                    personUtils.sendStartVictorinaMessage(person, victorina, sender)
                }
            }

        }
    }

    override fun forFree(textMessage: TextMessage, sender: MessageServicesSender) {
        personUtils.sendFreeMessage(textMessage.userId,  Locale.forLanguageTag(textMessage.user!!.languageCode) ,sender)
    }

    override fun getUtils(): PersonUtils {
        return personUtils
    }

}
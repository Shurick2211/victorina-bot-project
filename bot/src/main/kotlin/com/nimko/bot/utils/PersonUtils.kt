package com.nimko.bot.utils

import com.nimko.bot.models.Person
import com.nimko.bot.models.Quiz
import com.nimko.bot.models.VictorinaDto
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import com.nimko.messageservices.telegram.models.message.PollAnswer
import com.nimko.messageservices.telegram.models.message.TextMessage
import org.telegram.telegrambots.meta.api.objects.User
import java.util.*


interface PersonUtils {

    fun sendRegistrationFinishMessage(userId: String,lang:Locale, sender: MessageServicesSender)

    fun getPerson(userId:String): Person?

    fun savePerson(person: Person): Person?

    fun deleteInlineKeyboard(chatId:String, messageId:String, sender: MessageServicesSender)

    fun sendStartVictorinaMessage(person: Person, victorina: VictorinaDto, sender: MessageServicesSender)

    fun saveChannelForAdmin(channelIdMessage: ChannelIdMessage, sender: MessageServicesSender)

    fun sendFreeMessage(userId: String, lang: Locale, sender: MessageServicesSender)

    fun sendQuestion(person: Person, victorina:VictorinaDto, numQuestion:Int, sender: MessageServicesSender)

    fun checkUserAsChannelMember(channelId: String, userId: String, sender: MessageServicesSender):Boolean?

    fun checkVictorinaResult(quiz: Quiz, victorina: VictorinaDto):Quiz

    fun sendFinishQuizMessage(pollAnswer: PollAnswer, victorina: VictorinaDto, currentQuiz:Quiz ,sender: MessageServicesSender)

    fun sendBusyMessage(userId: String, lang: Locale, sender: MessageServicesSender)

    fun sendReadyMessage(mess:TextMessage, lang: Locale, sender: MessageServicesSender)

    fun sendStartRegMessage(userId:String, lang:Locale, sender: MessageServicesSender)

    fun sendStartMessage(user:User, sender: MessageServicesSender)
}
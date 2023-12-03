package com.nimko.bot.utils

import com.nimko.bot.models.Person
import com.nimko.bot.models.Quiz
import com.nimko.bot.models.Victorina
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.services.VictorinaServices
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.telegram.utils.CallbackData
import com.nimko.messageservices.telegram.utils.PollType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

@Component
class PersonUtilsImpl @Autowired constructor(
    private val personRepo: PersonRepo,
    private val messageSource: MessageSource,
    private val victorinaServices: VictorinaServices
):PersonUtils{

    @Value("\${my.address}") private lateinit var url:String

    val log = LoggerFactory.getLogger("PERS_UTIL")

   override fun sendRegistrationFinishMessage(userId: String,lang:Locale, sender: MessageServicesSender) {

        sender.sendTextAndInlineButton(TextMessage(userId,
            messageSource.getMessage("message.reg.finish",null, lang), null),
            listOf(InlineButton(messageSource.getMessage("button.link",null, lang),
                CallbackData.LINK.toString(), url="${url}?user=${userId}")
            )
        )
    }
    override fun getPerson(userId:String): Person? = personRepo.findById(userId).orElse(null)

    override fun savePerson(person: Person): Person? = personRepo.save(person)

    override fun deleteInlineKeyboard(chatId:String, messageId:String, sender: MessageServicesSender) {
        sender.sendChangeInlineButton(ChangeInlineMessage(chatId, messageId, emptyList()))
    }

    private fun isChannelUser(person: Person, victorina: Victorina, sender: MessageServicesSender):Boolean?{
        return if (victorina.channel != null) checkUserAsChannelMember(victorina.channel.channelId, person.id, sender)
        else true
    }

    override fun sendStartVictorinaMessage(person: Person, victorina: Victorina, sender: MessageServicesSender) {
        val mess = TextMessage(person.id, victorina.title,null)
        if (isChannelUser(person, victorina, sender) == true) {
            if (person.quizes == null) person.quizes = ArrayList()
            val quiz = Quiz(victorina.id!!, ArrayList())
            person.quizes!!.add(quiz)
            person.state = PersonState.IN_VICTORINA
            personRepo.save(person)
            sender.sendText(mess)
            sendQuestion(person, victorina, 0, sender)
        } else {
            sender.sendTextAndInlineButton(TextMessage(person.id,
                messageSource.getMessage("message.subscribe", null, Locale.forLanguageTag(person.languageCode))
                , null),
                listOf(InlineButton(victorina.channel!!.channelName,
                    "${CallbackData.SUBSCRIBE}#${victorina.id}", url = victorina.channel!!.url),
                    InlineButton(messageSource.getMessage("button.ready",null,
                        Locale.forLanguageTag(person.languageCode)),
                        "${CallbackData.QUIZ}#${victorina.id}"),
                ), 1
            )
        }

    }

    override fun saveChannelForAdmin(channelIdMessage: ChannelIdMessage, sender: MessageServicesSender) {
        val admin = getPerson(channelIdMessage.adminId)!!
        if (admin.channelsAdmin == null){
            admin.channelsAdmin = ArrayList()
        }
        if (admin.channelsAdmin!!.indexOf(channelIdMessage) == -1){
            channelIdMessage.channel.inviteLink =
                sender.getChat(channelIdMessage.channelId).inviteLink
            admin.channelsAdmin!!.add(channelIdMessage)
            personRepo.save(admin)
        }
    }

    override fun sendFreeMessage(userId: String, lang: Locale, sender: MessageServicesSender) {
        log.info(lang.toString())
        val listButton = ArrayList<InlineButton>()
        victorinaServices.getActiveVictorin().forEach {
            if(!isEndedVictorinaByUser(userId, it.id!!)) {
                var name = "${it.name} ${
                    messageSource.getMessage("message.from", null, lang)
                } - "
                name += if (it.channel !== null) it.channel.channelName
                else getPerson(it.ownerId)!!.firstName ?: getPerson(it.ownerId)!!.userName
                val button = InlineButton(name, it.id!!)
                listButton.add(button)
            }
        }
        if (listButton.isNotEmpty())
            sender.sendTextAndInlineButton(
                TextMessage(userId,
                    messageSource.getMessage("message.invite", null, lang),
                    null),
                listButton, 1
            )
        else sender.sendTextAndInlineButton(
            TextMessage(userId,
                messageSource.getMessage("message.invite.none", null, lang),
                null),
            listOf(
                InlineButton(
                messageSource.getMessage("button.try.again", null, lang),
                CallbackData.FREE.toString())
            )
        )
    }

    private fun isEndedVictorinaByUser(userId: String, victorinaId: String):Boolean{
        val person = getPerson(userId)!!
        return if (person.quizes != null) {
            person.quizes!!.map { it.victorinaId }.contains(victorinaId)
        } else  false
    }

    override fun  sendQuestion(person: Person, victorina:Victorina, numQuestion:Int, sender: MessageServicesSender){
        val type = if (victorina.isManyAnswer) PollType.REGULAR
             else  PollType.QUIZ
        sender.sendOnePoll(
            PollMessage(
                person.id, victorina.questions[numQuestion].text, victorina.questions[numQuestion].answers,
                victorina.questions[numQuestion].rightAnswer, null, type = type.getType()
            )
        )
    }
    override fun checkUserAsChannelMember(channelId: String, userId: String, sender: MessageServicesSender):Boolean? {
        return try{
            val status = sender.checkIsUserOfChannel(channelId, userId).status
            !(status == "kicked" || status == "left")
        } catch (e: TelegramApiRequestException){
            null
        }
    }

    override fun checkVictorinaResult(quiz: Quiz, victorina: Victorina):Quiz {
        var rA = 0
        for (i in 0 until quiz.userAnswers.size){
            if(quiz.userAnswers[i].equals(victorina.questions[i].rightAnswer.toList())) rA++
        }
        quiz.isRightAnswered = rA == quiz.userAnswers.size
        val res = rA.toDouble().div(quiz.userAnswers.size.toDouble()).times(100.0)
        quiz.percentRightAnswer = if (quiz.isRightAnswered!!) 100 else res.toInt()
        return quiz
    }

    override fun sendFinishQuizMessage(pollAnswer: PollAnswer, victorina: Victorina, currentQuiz:Quiz, sender: MessageServicesSender){
        val locale = Locale.forLanguageTag(pollAnswer.userLang)
        sender.sendText(
            TextMessage(pollAnswer.userId,
                "${messageSource.getMessage("message.end", null, locale)} " +
                        "${currentQuiz.percentRightAnswer}% \n " +
                        messageSource.getMessage("message.end.continuation", null, locale) +
                        " - ${victorina.endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale))}"
                ,null))
    }

    override fun sendBusyMessage(userId: String, lang: Locale, sender: MessageServicesSender){
        sender.sendText(
            TextMessage(userId,
                messageSource.getMessage("message.busy",null, lang)
                ,null)
        )
    }

    override fun sendReadyMessage(mess:TextMessage, lang: Locale, sender: MessageServicesSender){
        sender.sendTextAndInlineButton(mess,
            listOf(InlineButton(messageSource.getMessage("button.ready",null,
                lang), CallbackData.READY.toString()))
        )
    }
    override fun sendStartRegMessage(userId:String, lang:Locale, sender: MessageServicesSender){
        sender.sendTextAndInlineButton(
            TextMessage(userId, messageSource.getMessage("message.reg.start",
                    null,lang),null),
            listOf(InlineButton(messageSource.getMessage("button.ready",null,
                    lang), CallbackData.READY.toString()))
        )
    }

    override fun sendStartMessage(user:User, sender: MessageServicesSender){
        sender.sendMenu(
            MenuMessage(
                user.id.toString(), user.firstName + "! "
                        + messageSource.getMessage("message.start", null,
                    Locale.forLanguageTag(user.languageCode)
                ),
                listOf(
                    messageSource.getMessage("button.restart", null,
                        Locale.forLanguageTag(user.languageCode)
                    ),
                    messageSource.getMessage("button.for.creator", null,
                        Locale.forLanguageTag(user.languageCode)
                    ),
                    messageSource.getMessage("button.free.message", null,
                        Locale.forLanguageTag(user.languageCode)
                    )
                )
            )
        )
    }
}
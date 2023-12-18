package com.nimko.bot.utils

import com.nimko.bot.models.*
import com.nimko.bot.repositories.ChannelRepo
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.services.VictorinaServices
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.*
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.telegram.utils.CallbackData
import com.nimko.messageservices.telegram.utils.Commands
import com.nimko.messageservices.telegram.utils.PollType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

@Component
class PersonUtilsImpl @Autowired constructor(
    private val personRepo: PersonRepo,
    private val messageSource: MessageSource,
    private val victorinaServices: VictorinaServices,
    private val channelRepo: ChannelRepo
):PersonUtils{

    @Value("\${my.address}") private lateinit var url:String

    val log = LoggerFactory.getLogger("PERS_UTIL")!!

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

    private fun isChannelUser(person: Person, victorina: VictorinaDto, sender: MessageServicesSender):Boolean?{
        return if (victorina.channel != null) checkUserAsChannelMember(victorina.channel.channelId, person.id, sender)
        else true
    }

    override fun sendStartVictorinaMessage(person: Person, victorina: VictorinaDto, sender: MessageServicesSender) {
        val nameOfOwner = if (victorina.channel != null) "<a href=\"${victorina.channel.url}\">${victorina.channel.channelName}</a>"
        else "<b>${getPerson(victorina.ownerId)!!.firstName}</b>"
        val mess = TextMessage(person.id,
            "<b>${ victorina.name }</b> " +
                    messageSource.getMessage("message.from", null, Locale.forLanguageTag(person.languageCode)) + " " +
                    nameOfOwner +
                    "\n\n" + victorina.title,null)
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
                listOf(InlineButton(victorina.channel!!.channelName!!,
                    "${CallbackData.SUBSCRIBE}#${victorina.id}", url = victorina.channel!!.url),
                    InlineButton(messageSource.getMessage("button.ready",null,
                        Locale.forLanguageTag(person.languageCode)),
                        "${CallbackData.QUIZ}#${victorina.id}"),
                ), 1
            )
        }

    }

    override fun saveChannelForAdmin(channelIdMessage: ChannelIdMessage, sender: MessageServicesSender) {
        if (sender.checkIsUserOfChannel(channelIdMessage.channelId,channelIdMessage.adminId) is ChatMemberAdministrator ||
            sender.checkIsUserOfChannel(channelIdMessage.channelId,channelIdMessage.adminId) is ChatMemberOwner) {
            val admin = getPerson(channelIdMessage.adminId)!!
            if (admin.channelsAdmin == null) {
                admin.channelsAdmin = ArrayList()
            }
            val indChatOnAdmin = admin.channelsAdmin!!.indexOf(channelIdMessage)
            val chat = sender.getChat(channelIdMessage.channelId)
            channelIdMessage.channel.inviteLink =
                if (chat.inviteLink != null) chat.inviteLink
                else "https://t.me/" + chat.userName
            if (indChatOnAdmin == -1) {
                admin.channelsAdmin!!.add(channelIdMessage)
            } else admin.channelsAdmin!!.set(indChatOnAdmin, channelIdMessage)
            channelRepo.save(ChannelEntity(chat.id.toString(), chat.title,
                    channelIdMessage.channel.inviteLink))
            personRepo.save(admin)
        } else
            sender.sendText(TextMessage(channelIdMessage.adminId,
                messageSource.getMessage("message.not.admin",null,Locale.forLanguageTag(channelIdMessage.user.languageCode))
                ,null))
    }

    override fun sendFreeMessage(userId: String, lang: Locale, sender: MessageServicesSender) {
        log.info(lang.toString())
        val listButton = ArrayList<InlineButton>()
        victorinaServices.getActiveVictorin().forEach {
            if(!isEndedVictorinaByUser(userId, it.id!!)) {
                val button = InlineButton(it.name, it.id!!)
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

    override fun  sendQuestion(person: Person, victorina:VictorinaDto, numQuestion:Int, sender: MessageServicesSender){
        val type = if (victorina.isManyAnswer) PollType.REGULAR
             else  PollType.QUIZ
        sender.sendOnePoll(
            PollMessage(
                person.id, victorina.questions[numQuestion].text, victorina.questions[numQuestion].answers,
                victorina.questions[numQuestion].rightAnswer, victorina.questions[numQuestion].explanation, type = type.getType()
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

    override fun checkVictorinaResult(quiz: Quiz, victorina: VictorinaDto):Quiz {
        var rA = 0
        for (i in 0 until quiz.userAnswers.size){
            if(quiz.userAnswers[i] == victorina.questions[i].rightAnswer.toList()) rA++
        }
        quiz.isRightAnswered = rA == quiz.userAnswers.size
        val res = rA.toDouble().div(quiz.userAnswers.size.toDouble()).times(100.0)
        quiz.percentRightAnswer = if (quiz.isRightAnswered!!) 100 else res.toInt()
        return quiz
    }

    override fun sendFinishQuizMessage(pollAnswer: PollAnswer, victorina: VictorinaDto, currentQuiz:Quiz, sender: MessageServicesSender){
        val locale = Locale.forLanguageTag(pollAnswer.userLang)
        victorinaServices.addParticipants(pollAnswer.userId, victorina.id!!)
        sender.sendText(
            TextMessage(pollAnswer.userId,
                "${messageSource.getMessage("message.end", null, locale)} " +
                        "${currentQuiz.percentRightAnswer}% \n " +
                        if(victorina.hasPrize) {
                            messageSource.getMessage("message.end.continuation", null, locale) +
                            " - ${ victorina.endDate.format(
                                    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale)
                                ) }"}
                        else ""
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
        val commands = listOf(BotCommand(Commands.START.getCommand(),messageSource.getMessage("button.restart", null,
            Locale.forLanguageTag(user.languageCode))),
            BotCommand(Commands.CREATOR.getCommand(), messageSource.getMessage("button.for.creator", null,
                Locale.forLanguageTag(user.languageCode))),
            BotCommand(Commands.REFRESH.getCommand(), messageSource.getMessage("button.free.message", null,
                Locale.forLanguageTag(user.languageCode)))
            )
        sender.sendText(TextMessage(user.id.toString(),"  " +
            messageSource.getMessage("message.start", null, Locale.forLanguageTag(user.languageCode))
            ,null))
        sender.setMenu(commands, user.languageCode)
    }

    override fun sendVictorinaWinnerMessage(winner:Person,
        victorina: VictorinaDto,
        sender: MessageServicesSender
    ) {
        victorina.rightsAnsweredUserId!!.forEach { userId ->
            val person = getPerson(userId)!!
            if (userId == winner.id)
                sender.sendTextAndInlineButton(
                    TextMessage(winner.id,
                        "<b>"+victorina.name + "</b> " +
                        messageSource.getMessage("message.for.winner", null,
                            Locale.forLanguageTag(winner.languageCode))
                        , null),
                    listOf(
                        InlineButton(
                            messageSource.getMessage("button.prize", null,
                                Locale.forLanguageTag(winner.languageCode)), CallbackData.PRIZE.toString()
                        )
                    )
                )
             else sender.sendText(
                TextMessage(userId,
                    "<b>"+victorina.name + "</b> " +
                    messageSource.getMessage("message.victorina.winner", null,
                        Locale.forLanguageTag(person.languageCode)) + victorina.rightsAnsweredUserId!!.size + " " +
                            messageSource.getMessage("message.victorina.winner.2", null,
                                Locale.forLanguageTag(person.languageCode))  + (victorina.rightsAnsweredUserId!!.indexOf(winner.id)+1).toString() + " \n" +
                            messageSource.getMessage("message.victorina.winner.3", null,
                                Locale.forLanguageTag(person.languageCode)) + winner.firstName + " " + (winner.lastName ?: "") + "!"
                    , null)
            )
        }
        victorinaServices.saveWinner(winner.id, victorina.id!!)
    }

    override fun sendDeliveryAddress(winnerMess: TextMessage, sender: MessageServicesSender) {
        val victorina = victorinaServices.getVictorinaByWinnerId(winnerMess.userId)
        val owner = getPerson(victorina.ownerId)!!
        sender.sendText(TextMessage(owner.id,
            victorina.name + messageSource.getMessage("message.owner", null, Locale.forLanguageTag(owner.languageCode)) +
                    winnerMess.user!!.userName +" : " + winnerMess.textMessage
            ,null))
    }


    @Value("\${name.telega}")
    lateinit var nameBot:String
    override fun sendMessageForStartVictorina(
        victorina: VictorinaDto,
        sender: MessageServicesSender
    ) {
        val owner = getPerson(victorina.ownerId)!!
        val receiverId:String
            if (victorina.channel != null) receiverId = victorina.channel.channelId
            else {
                receiverId = owner.id
                sender.sendText(TextMessage(owner.id,
                    messageSource.getMessage("message.person.invite",null, Locale.forLanguageTag(owner.languageCode)),
                    null))
            }
        sender.sendTextAndInlineButton(TextMessage(receiverId,"<b>" + victorina.name + "</b> \n"+
        victorina.title +
        messageSource.getMessage("message.channel.start", null, Locale.forLanguageTag(owner.languageCode)), null)
            , listOf(
                InlineButton(
                messageSource.getMessage("button.channel.start", null, Locale.forLanguageTag(owner.languageCode)),
                    CallbackData.QUIZ.name+"#"+victorina.id!!, url = "https://t.me/" + nameBot + "?start=" + victorina.id!!
            )
            ))
    }


}
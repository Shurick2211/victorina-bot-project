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

@Component
class PersonServicesImpl @Autowired constructor(val personRepo:PersonRepo):PersonServices{

    final val START_MESSAGE = "Hello in Quiz-bot! "

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
        sender.sendTextAndInlineButton(
            TextMessage(user.id.toString(),
                "Have victorina press start or no!", user),
            listOf(
                InlineButton("Start", "start"),
                InlineButton("No", "free")
            )
        )

    }

    override fun registrationCreator(
        person: Person?,
        responseDataMessage: ResponseDataMessage?,
        channelIdMessage: ChannelIdMessage?,
        sender: MessageServicesSender
    ) {
        println(person)
    }

    override fun onQuiz(person: Person?,
                        responseDataMessage: ResponseDataMessage?,
                        pollAnswer: PollAnswer?,
                        sender: MessageServicesSender
    ) {
        responseDataMessage?.let {
            if(responseDataMessage.callbackQuery.data == "start")
                sender.sendChangeInlineButton(
                    ChangeInlineMessage(responseDataMessage.chatId,
                    responseDataMessage.callbackQuery.message.messageId.toString(), emptyList()
                    )
                )
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

    override fun forFree(textMessage: TextMessage, sender: MessageServicesSender) {
        println(textMessage)
    }

    override fun getPerson(userId: String): Person? = personRepo.findById(userId).orElse(null)

}
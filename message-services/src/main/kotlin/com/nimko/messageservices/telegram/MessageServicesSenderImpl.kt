package com.nimko.messageservices.telegram

import com.nimko.messageservices.telegram.models.message.ChangeInlineMessage
import com.nimko.messageservices.telegram.models.message.MenuMessage
import com.nimko.messageservices.telegram.models.message.PollMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import com.nimko.messageservices.telegram.models.others.InlineButton
import com.nimko.messageservices.services.MessageServicesSender
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


class MessageServicesSenderImpl(
    val bot: TelegramBot
):MessageServicesSender {


    override fun sendText(message: TextMessage) {
        bot.execute(createMessage(message.userId, message.textMessage))
    }

    override fun sendTextAndInlineButton(textMessage: TextMessage, buttons: List<InlineButton>) {
        val sendMessage = createMessage(textMessage.userId, textMessage.textMessage)
        sendMessage.replyMarkup = createInlineButton(buttons)
        bot.execute(sendMessage)
    }

    override fun sendChangeInlineButton(message: ChangeInlineMessage) {
        val answer = EditMessageReplyMarkup()
        answer.chatId = message.userId
        answer.messageId = message.messageId.toInt()
        answer.replyMarkup = createInlineButton(message.buttons)
        bot.execute(answer)
    }

    override fun sendMenu(menu: MenuMessage) {
        val menuSend = createMessage(menu.userId, menu.title)
        menuSend.replyMarkup = createMenuButton(menu.buttonNames)
        bot.execute(menuSend)
    }

    override fun sendOnePoll(poll: PollMessage) {
        bot.execute(createPoll(poll))
    }

    private fun createMessage(userId:String, text:String): SendMessage{
        val sendMessage = SendMessage()
        sendMessage.chatId = userId
        sendMessage.text = text
        sendMessage.enableHtml(true)
        sendMessage.enableMarkdown(true)
        return sendMessage
    }

    private fun createPoll(poll: PollMessage): SendPoll{
        val pollMessage = SendPoll()
        pollMessage.chatId = poll.chatId
        pollMessage.question = poll.question
        pollMessage.options = poll.options
        pollMessage.correctOptionId = poll.correctOption
        pollMessage.allowMultipleAnswers = false
        pollMessage.type="quiz"
        pollMessage.isAnonymous = false
        poll.explanation?.let { pollMessage.explanation = it }
        return pollMessage
    }

    private fun createInlineButton(buttons: List<InlineButton>):InlineKeyboardMarkup{
        val BUTTONS_ON_ROW = 2
        val keyboard = ArrayList<MutableList<InlineKeyboardButton>>()
        var buttonsRow:MutableList<InlineKeyboardButton> = ArrayList(BUTTONS_ON_ROW)
        buttons.forEach{
            val button = InlineKeyboardButton()
            button.text = it.name
            button.callbackData = it.responseData

            buttonsRow.add(button)

            if(buttonsRow.size == BUTTONS_ON_ROW) {
                keyboard.add(buttonsRow)
                buttonsRow = ArrayList(BUTTONS_ON_ROW)
            }

        }
        if (!buttonsRow.isEmpty()) keyboard.add(buttonsRow)
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = keyboard
        return inlineKeyboardMarkup;
    }

    private fun createMenuButton(buttons:List<String>):ReplyKeyboardMarkup{
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = false

        val keyboard = ArrayList<KeyboardRow>()
        buttons.forEach {
            val keyboardRow =  KeyboardRow()
            keyboardRow.add(it)
            keyboard.add(keyboardRow)
        }
        replyKeyboardMarkup.keyboard = keyboard
        return replyKeyboardMarkup
    }



}
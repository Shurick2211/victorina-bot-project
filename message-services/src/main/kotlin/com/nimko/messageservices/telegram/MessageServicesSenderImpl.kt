package com.nimko.messageservices.telegram

import com.nimko.messageservices.models.message.ChangeInlineMessage
import com.nimko.messageservices.models.message.MenuMessage
import com.nimko.messageservices.models.message.PollMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.models.others.InlineButton
import com.nimko.messageservices.services.MessageServicesSender
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll.SendPollBuilder
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


class MessageServicesSenderImpl(
    bot: TelegramBot
):TelegramSender(bot),MessageServicesSender {


    override fun sendText(message: TextMessage) {
        sendMessage(createMessage(message.userId, message.textMessage))
    }

    override fun sendTextAndInlineButton(textMessage: TextMessage, buttons: List<InlineButton>) {
        val sendMessage = createMessage(textMessage.userId, textMessage.textMessage)
        sendMessage.replyMarkup = createInlineButton(buttons)
        sendMessage(sendMessage)
    }

    override fun sendChangeInlineButton(message: ChangeInlineMessage) {
        val answer = EditMessageReplyMarkup()
        answer.chatId = message.userId
        answer.messageId = message.messageId.toInt()
        answer.replyMarkup = createInlineButton(message.buttons)
        sendAnswerInline(answer)
    }

    override fun sendMenu(menu: MenuMessage) {
        val menuSend = createMessage(menu.userId, menu.title)
        menuSend.replyMarkup = createMenuButton(menu.buttonNames)
        sendMessage(menuSend)
    }

    override fun sendOnePoll(poll: PollMessage) {
        sendPoll(createPoll(poll))
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
        pollMessage.explanation = "Ha-ha-ha!"
        return pollMessage
    }

    private fun createInlineButton(buttons: List<InlineButton>):InlineKeyboardMarkup{
        val keyboard = ArrayList<MutableList<InlineKeyboardButton>>()
        var buttonsRow:MutableList<InlineKeyboardButton> = ArrayList(2)
        buttons.forEach{
            val button = InlineKeyboardButton()
            button.text = it.name
            button.callbackData = it.responseData

            buttonsRow.add(button)

            if(buttonsRow.size == 2) {
                keyboard.add(buttonsRow)
                buttonsRow = ArrayList(2)
            }

        }
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = keyboard
        println(keyboard)
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

    override fun sendMessage(sendMessage: SendMessage) {
        bot.execute(sendMessage)
    }

    override fun sendAnswerInline(answer: EditMessageReplyMarkup) {
        bot.execute(answer)
    }

    override fun sendPoll(poll: SendPoll) {
        bot.execute(poll)
    }

}
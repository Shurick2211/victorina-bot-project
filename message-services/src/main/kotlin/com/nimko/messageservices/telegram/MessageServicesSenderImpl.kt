package com.nimko.messageservices.telegram

import com.nimko.messageservices.models.message.ChangeInlineMessage
import com.nimko.messageservices.models.message.MenuMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.models.others.InlineButton
import com.nimko.messageservices.services.MessageServicesSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

@Service
class MessageServicesSenderImpl(
    @Autowired val sender:TelegramSendable
):MessageServicesSender {
    override fun sendText(message: TextMessage) {
        sender.sendMessage(createMessage(message.userId, message.textMessage))
    }

    override fun sendTextAndInlineButton(textMessage: TextMessage, buttons: List<InlineButton>) {
        val sendMessage = createMessage(textMessage.userId, textMessage.textMessage)
        sendMessage.replyMarkup = createInlineButton(buttons)
        sender.sendMessage(sendMessage)
    }

    override fun sendChangeInlineButton(message: ChangeInlineMessage) {
        val answer = EditMessageReplyMarkup()
        answer.chatId = message.userId
        answer.replyMarkup = createInlineButton(message.buttons)
        sender.sendAnswerInline(answer)
    }

    override fun sendMenu(menu: MenuMessage) {
        val menuSend = createMessage(menu.userId, menu.title)
        menuSend.replyMarkup = createMenuButton(menu.buttonNames)
        sender.sendMessage(menuSend)
    }

    private fun createMessage(userId:String, text:String): SendMessage{
        val sendMessage = SendMessage()
        sendMessage.chatId = userId
        sendMessage.text = text
        sendMessage.enableHtml(true)
        sendMessage.enableMarkdown(true)
        return sendMessage
    }

    private fun createInlineButton(buttons: List<InlineButton>):InlineKeyboardMarkup{
        val keyboard = ArrayList<MutableList<InlineKeyboardButton>>()
        var i = 1
        buttons.forEach{
            val button = InlineKeyboardButton()
            button.text = it.name
            button.switchInlineQuery = it.responseData

            val buttonsRow:MutableList<InlineKeyboardButton> = ArrayList(2)
            when(i%2){
                 0 -> {
                     buttonsRow.add(button)
                     keyboard.add(buttonsRow)
                 }
                 1 -> {
                     buttonsRow.clear()
                     buttonsRow.add(button)
                 }
            }
            i++
        }
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
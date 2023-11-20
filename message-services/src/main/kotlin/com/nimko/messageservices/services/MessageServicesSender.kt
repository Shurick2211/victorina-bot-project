package com.nimko.messageservices.services

import com.nimko.messageservices.models.message.ChangeInlineMessage
import com.nimko.messageservices.models.message.MenuMessage
import com.nimko.messageservices.models.message.PollMessage
import com.nimko.messageservices.models.message.TextMessage
import com.nimko.messageservices.models.others.InlineButton

interface MessageServicesSender {

    fun sendText(message: TextMessage)

    fun sendTextAndInlineButton(textMessage: TextMessage, buttons:List<InlineButton>)

    fun sendChangeInlineButton(message:ChangeInlineMessage)

    fun sendMenu(menu:MenuMessage)

    fun sendOnePoll(poll:PollMessage)
}
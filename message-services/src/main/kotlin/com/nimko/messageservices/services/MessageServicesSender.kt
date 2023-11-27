package com.nimko.messageservices.services

import com.nimko.messageservices.telegram.models.message.ChangeInlineMessage
import com.nimko.messageservices.telegram.models.message.MenuMessage
import com.nimko.messageservices.telegram.models.message.PollMessage
import com.nimko.messageservices.telegram.models.message.TextMessage
import com.nimko.messageservices.telegram.models.others.InlineButton
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember

interface MessageServicesSender {
    fun sendText(message: TextMessage)

    fun sendTextAndInlineButton(textMessage: TextMessage, buttons:List<InlineButton>, buttonRows:Int = 2)

    fun sendChangeInlineButton(message: ChangeInlineMessage, buttonRows:Int = 2)

    fun sendMenu(menu: MenuMessage)

    fun sendOnePoll(poll: PollMessage)

    fun checkIsUserOfChannel(chatId:String, userId:String): ChatMember

    fun createInviteChannelLink(channelId: String):ChatInviteLink

    fun getInviteLink(chanelId:String):String
}
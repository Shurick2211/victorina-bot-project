package com.nimko.messageservices.telegram.models.message

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User

data class ChannelIdMessage (
    val adminId:String,
    val channelId:String,
    val channel:Chat,
    val user: User
){
    override fun equals(other: Any?): Boolean {
        return other != null && other is ChannelIdMessage && channelId == other.channelId
    }

    override fun hashCode(): Int {
        return channelId.hashCode()
    }
}
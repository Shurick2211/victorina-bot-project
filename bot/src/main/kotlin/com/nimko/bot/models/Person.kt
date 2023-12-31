package com.nimko.bot.models

import com.nimko.bot.utils.PersonRole
import com.nimko.bot.utils.PersonState
import com.nimko.messageservices.telegram.models.message.ChannelIdMessage
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("persons")
data class Person(
    @Id
    val id:String,
    var firstName:String?,
    var lastName:String?,
    val userName:String,
    var languageCode:String,
    var channelsAdmin:MutableList<ChannelIdMessage>?,
    var quizes:MutableList<Quiz>?,
    var state:PersonState,
    var role:PersonRole,
    var password:String?
){
    fun toDto() = PersonDto(
        this.id,
        this.firstName,
        this.lastName,
        this.userName,
        this.languageCode,
        this.channelsAdmin?.map{ch -> Channel(ch.channelId, ch.channel.title, ch.channel.inviteLink)}?.toTypedArray(),
        this.quizes?.toTypedArray(),
        this.role,
        this.password
    )
}

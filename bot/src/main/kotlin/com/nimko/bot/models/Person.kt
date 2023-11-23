package com.nimko.bot.models

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
    val quizes:MutableList<Quiz>?,
    var state:PersonState
)

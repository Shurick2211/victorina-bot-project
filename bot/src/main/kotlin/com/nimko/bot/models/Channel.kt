package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("channels")
data class Channel(
    @Id
    val channelId:String,
    val channelName:String,
    val url:String
)

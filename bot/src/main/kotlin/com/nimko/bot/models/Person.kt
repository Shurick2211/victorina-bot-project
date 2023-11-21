package com.nimko.bot.models

import com.nimko.bot.utils.PersonState
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
    var channelsIdAdmin:MutableList<String>?,
    val quizes:MutableList<Quiz>?,
    var state:PersonState
)

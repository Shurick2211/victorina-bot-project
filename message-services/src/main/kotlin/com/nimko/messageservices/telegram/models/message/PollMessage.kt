package com.nimko.messageservices.telegram.models.message

import com.nimko.messageservices.telegram.utils.PollType

data class PollMessage(
    val chatId:String,
    val question:String,
    val options:Array<String>,
    val correctOption:Array<Int>,
    val explanation:String?,
    val type:String = PollType.QUIZ.getType()
)

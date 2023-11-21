package com.nimko.messageservices.models.message

data class PollMessage(
    val chatId:String,
    val question:String,
    val options:List<String>,
    val correctOption:Int,
    val explanation:String?
)

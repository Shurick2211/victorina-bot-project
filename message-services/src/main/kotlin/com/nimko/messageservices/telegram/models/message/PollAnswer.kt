package com.nimko.messageservices.telegram.models.message

data class PollAnswer(
    val userId: String,
    val pollId:String,
    val answers: List<Int>,
)

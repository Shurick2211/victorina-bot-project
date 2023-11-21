package com.nimko.bot.models

data class Quiz(
    val victorinaId:String,
    val userAnswers:MutableList<Int>,
    var isWinner:Boolean?
)

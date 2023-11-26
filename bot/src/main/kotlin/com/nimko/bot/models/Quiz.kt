package com.nimko.bot.models

data class Quiz(
    val victorinaId:String,
    val userAnswers:MutableList<Array<Int>>,
    var isWinner:Boolean?
)

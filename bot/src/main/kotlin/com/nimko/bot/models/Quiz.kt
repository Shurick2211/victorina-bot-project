package com.nimko.bot.models

data class Quiz(
    val victorinaId:String,
    val userAnswers:MutableList<List<Int>>,
    var isRightAnswered:Boolean? = null,
    var percentRightAnswer:Int = 0
)

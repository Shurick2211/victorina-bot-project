package com.nimko.bot.models

data class Question(
    val text:String ,
    val explanation:String? = null,
    val answers:Array<String>,
    val rightAnswer:Array<Int>
)

package com.nimko.bot.models

data class Question(
    val text:String ,
    val answers:Array<String>,
    val rightAnswer:Int
)

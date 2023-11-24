package com.nimko.bot.models


data class PersonDto(
    val id:String,
    val firstName:String?,
    val lastName:String?,
    val userName:String,
    val languageCode:String,
    val channelsAdmin:Array<String>?,
    val quizes: Array<Quiz>?,
    val password:String?
){

}

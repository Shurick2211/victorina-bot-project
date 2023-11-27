package com.nimko.bot.models

import com.nimko.bot.utils.PersonRole


data class PersonDto(
    val id:String,
    val firstName:String?,
    val lastName:String?,
    val userName:String,
    val languageCode:String,
    val channelsAdmin:Array<Channel>?,
    val quizes: Array<Quiz>?,
    var role: PersonRole,
    val password:String?
){

}

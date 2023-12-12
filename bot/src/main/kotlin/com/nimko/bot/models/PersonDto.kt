package com.nimko.bot.models

import com.nimko.bot.utils.PersonRole


data class PersonDto(
    var id:String? = null,
    var firstName:String? = null,
    var lastName:String? = null,
    var userName:String? = null,
    var languageCode:String? =null,
    var channelsAdmin:Array<Channel>? = null,
    var quizes: Array<Quiz>? = null,
    var role: PersonRole? = null,
    var password:String? = null
){

}

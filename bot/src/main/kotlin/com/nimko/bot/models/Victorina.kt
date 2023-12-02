package com.nimko.bot.models

import java.time.LocalDateTime


data class Victorina(
    val id:String?,
    val name:String,
    val title:String,
    val questions:Array<Question>,
    val ownerId:String,
    val channel:Channel?,
    val isManyAnswer:Boolean,
    var isActive:Boolean,
    val startDate:LocalDateTime,
    val endDate:LocalDateTime
) {
}
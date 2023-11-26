package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("victorinas")
data class VictorinaDto(
    @Id
    var id:String? = null,
    val name:String,
    val title:String,
    val questions:Array<Question>,
    val ownerId:String,
    val chanelName:String?,
    val winnerId:String?,
    val startDate:String?,
    val endDate:String?
) {
}
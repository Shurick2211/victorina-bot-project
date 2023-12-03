package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("victorinas")
data class VictorinaDto(
    @Id
    var id:String? = null,
    val name:String,
    val title:String,
    val questions:Array<Question>,
    val ownerId:String,
    val channel:Channel?,
    var winnerId:String?,
    var rightsAnsweredUserId: MutableList<String>?,
    var isActive:Boolean,
    val isManyAnswer:Boolean,
    val startDate:LocalDateTime,
    val endDate:LocalDateTime
) {
}
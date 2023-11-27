package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    val startDate:String,
    val endDate:String
) {
    fun toVictorina():Victorina{
        return Victorina(
            id=id,
            name = name,
            title = title,
            questions = questions,
            ownerId = ownerId,
            channel = channel,
            startDate = LocalDateTime.ofInstant(
                Instant.from( DateTimeFormatter.ISO_INSTANT.parse(startDate)), ZoneId.systemDefault()),
            endDate = LocalDateTime.ofInstant(
                Instant.from( DateTimeFormatter.ISO_INSTANT.parse(endDate)), ZoneId.systemDefault())
        )
    }

}
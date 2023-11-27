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
    val chanelName:String?,
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
            chanelName = chanelName,
            startDate = LocalDateTime.parse(startDate),
            endDate = LocalDateTime.parse(endDate)
        )
    }

}
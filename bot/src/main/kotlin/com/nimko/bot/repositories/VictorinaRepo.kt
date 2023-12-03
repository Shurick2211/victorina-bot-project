package com.nimko.bot.repositories


import com.nimko.bot.models.VictorinaDto
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface VictorinaRepo: MongoRepository<VictorinaDto,String>{
    fun findAllByOwnerId(id:String):List<VictorinaDto>
    fun findAllByIsActiveIsTrueAndEndDateBefore(date: LocalDateTime):List<VictorinaDto>

    fun findAllByEndDateAfter(date: LocalDateTime):List<VictorinaDto>


}
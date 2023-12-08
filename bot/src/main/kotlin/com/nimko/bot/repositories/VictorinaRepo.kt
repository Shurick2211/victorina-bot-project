package com.nimko.bot.repositories


import com.nimko.bot.models.VictorinaDto
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface VictorinaRepo: MongoRepository<VictorinaDto,String>{
    fun findAllByOwnerId(id:String):List<VictorinaDto>

    fun findAllByIsActiveIsFalseAndEndDateAfter(date: LocalDateTime):List<VictorinaDto>

    fun findAllByIsActiveIsTrueAndHasPrizeIsTrueAndEndDateBefore(date: LocalDateTime):List<VictorinaDto>

    fun findAllByEndDateAfter(date: LocalDateTime):List<VictorinaDto>

    fun findByIsActiveIsTrueAndWinnerId(winnerId:String):VictorinaDto


}
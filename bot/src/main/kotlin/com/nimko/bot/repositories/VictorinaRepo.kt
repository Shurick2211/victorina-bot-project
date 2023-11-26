package com.nimko.bot.repositories


import com.nimko.bot.models.VictorinaDto
import org.springframework.data.mongodb.repository.MongoRepository

interface VictorinaRepo: MongoRepository<VictorinaDto,String>{

}